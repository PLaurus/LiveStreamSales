package com.example.livestreamsales.authorization

import com.example.livestreamsales.BuildConfig
import com.example.livestreamsales.di.components.app.modules.reactivex.qualifiers.ComputationScheduler
import com.example.livestreamsales.di.components.app.modules.reactivex.qualifiers.MainThreadScheduler
import com.example.livestreamsales.di.components.authorizeduser.AuthorizedUserComponent
import com.example.livestreamsales.model.application.authorization.LogInResult
import com.example.livestreamsales.repository.authorization.IAuthorizationRepository
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AuthorizationManager @Inject constructor(
    private val authorizationRepository: IAuthorizationRepository,
    private val authorizedUserComponentFactory: AuthorizedUserComponent.Factory,
    @MainThreadScheduler
    private val mainThreadScheduler: Scheduler,
    @ComputationScheduler
    private val computationScheduler: Scheduler
): IAuthorizationManager {
    companion object{
        private const val CODE_REQUEST_MAX_WAITING_TIME = 60L
    }

    private val disposables = CompositeDisposable()
    private val isUserLoggedInSubject = BehaviorSubject.createDefault(false)

    private var codeRequestTimer: Disposable? = null

    override var authorizedUserComponent: AuthorizedUserComponent? = null
        private set

    override val isUserLoggedIn: Observable<Boolean> = isUserLoggedInSubject.distinctUntilChanged()

    override val nextCodeRequestWaitingTime: BehaviorSubject<Long> = BehaviorSubject.create()
    override val isCodeRequestAvailable: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(true).apply {
        nextCodeRequestWaitingTime
            .map{ it <= 0 }
            .distinctUntilChanged()
            .observeOn(mainThreadScheduler)
            .subscribe(::onNext)
            .addTo(disposables)
    }

    override fun sendVerificationCodeRequest(
        telephoneNumber: String
    ): Maybe<Boolean> {
        if(BuildConfig.IgnoreRestRequests){
            startCodeRequestTimer()
            return Maybe.just(true)
        }

        if(isCodeRequestAvailable.value){
            return authorizationRepository.sendVerificationCodeRequest(telephoneNumber)
                .doOnSuccess{
                    if(it && isCodeRequestAvailable.value) startCodeRequestTimer()
                }
        }

        return Maybe.empty()
    }

    override fun getVerificationCodeLength(): Single<Int> {
        return authorizationRepository.getVerificationCodeLength()
    }

    override fun logIn(
        phoneNumber: String,
        verificationCode: Int
    ): Single<LogInResult> {

        val phoneVerificationMaybe =
            authorizationRepository.verifyPhoneNumber(phoneNumber, verificationCode)

        return phoneVerificationMaybe
            .flatMap{ verificationResult ->
                val token = verificationResult.token
                if(token == null){
                    Maybe.just(LogInResult(
                        isLoggedIn = false,
                        needRegistration = false,
                        errorMessage = verificationResult.errorMessage
                    ))
                } else{
                    val authorizedUserComponent = createAuthorizedUserComponent(token)

                    checkDoesUserNeedRegistration(authorizedUserComponent)
                        .map{ isUserNameCorrect ->
                            LogInResult(
                                isLoggedIn = true,
                                needRegistration = !isUserNameCorrect
                            )
                        }
                }
            }
            .defaultIfEmpty(LogInResult(
                isLoggedIn = false,
                needRegistration = false
            ))
    }

    private fun checkDoesUserNeedRegistration(authorizedUserComponent: AuthorizedUserComponent): Maybe<Boolean>{
        val userRepository = authorizedUserComponent.userRepository()

        val userInformationObservable = userRepository
            .getUserInformation()
            .lastElement()

        val userNameLengthThresholdSingle: Single<Pair<Int, Int>> = userRepository
            .getMinUserNameLength()
            .zipWith(userRepository.getMaxUserNameLength(),{ min, max ->
                min to max
            })


        return userInformationObservable
            .zipWith(
                userNameLengthThresholdSingle.toMaybe(),
                { userInformation, (minNameLength, maxNameLength) ->
                    userInformation.name.length in minNameLength..maxNameLength
                }
            )
    }

    override fun logOut(): Completable {
        return Completable.create { emitter ->
            authorizedUserComponent?.userRepository()?.processDataOnLogOut()
            destroyAuthorizedUserComponent()
            emitter.onComplete()
        }
    }

    private fun createAuthorizedUserComponent(token: String): AuthorizedUserComponent{
        destroyAuthorizedUserComponent()

        val authorizedUserComponent = authorizedUserComponentFactory.create(token)

        this.authorizedUserComponent = authorizedUserComponent
        isUserLoggedInSubject.onNext(true)

        return authorizedUserComponent
    }

    private fun destroyAuthorizedUserComponent(){
        isUserLoggedInSubject.onNext(false)
        authorizedUserComponent = null
    }

    private fun startCodeRequestTimer(){
        codeRequestTimer?.dispose()

        codeRequestTimer = Observable
            .intervalRange(
                0L,
                CODE_REQUEST_MAX_WAITING_TIME + 1,
                0L,
                1L,
                TimeUnit.SECONDS
            )
            .map{ CODE_REQUEST_MAX_WAITING_TIME - it }
            .subscribeOn(computationScheduler)
            .observeOn(mainThreadScheduler)
            .subscribe(nextCodeRequestWaitingTime::onNext)
            .addTo(disposables)
    }
}