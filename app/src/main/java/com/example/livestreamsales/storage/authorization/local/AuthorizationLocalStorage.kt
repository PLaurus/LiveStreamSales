package com.example.livestreamsales.storage.authorization.local

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.livestreamsales.di.components.app.modules.database.qualifiers.AuthorizationSharedPreferences
import com.example.livestreamsales.di.components.app.modules.reactivex.qualifiers.ComputationScheduler
import com.example.livestreamsales.di.components.app.modules.reactivex.qualifiers.IoScheduler
import com.example.livestreamsales.di.components.app.subscomponents.authorizeduser.AuthorizedUserComponent
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AuthorizationLocalStorage @Inject constructor(
    private val authorizedUserComponentFactory: AuthorizedUserComponent.Factory,
    @AuthorizationSharedPreferences
    private val authorizationSharedPreferences: SharedPreferences,
    @IoScheduler
    private val ioScheduler: Scheduler,
    @ComputationScheduler
    private val computationScheduler: Scheduler
): IAuthorizationLocalStorage {
    companion object{
        private const val TOKEN_SHARED_PREFERENCES_KEY = "authorization_token"
        private const val NEXT_CODE_REQUEST_DATE_SHARED_PREFERENCES_KEY = "next_code_request_date"
        private const val NEXT_CODE_REQUEST_TIME_ZONE_SHARED_PREFERENCES_KEY = "next_code_request_date_time_zone"
        private const val DEFAULT_NEXT_CODE_REQUEST_REQUIRED_WAITING_TIME = 60L
        private const val DEFAULT_CODE_LENGTH = 6
    }

    private val disposables = CompositeDisposable()
    private val isUserLoggedInSubject = BehaviorSubject.create<Boolean>()

    private var nextCodeRequestRequiredWaitingTime = DEFAULT_NEXT_CODE_REQUEST_REQUIRED_WAITING_TIME
    private var codeLength: Int = DEFAULT_CODE_LENGTH
    private var codeRequestTimer: Disposable? = null

    override val isUserLoggedIn: Observable<Boolean> = isUserLoggedInSubject.distinctUntilChanged()

    override var authorizedUserComponent: AuthorizedUserComponent? = null
        private set

    override val nextCodeRequestWaitingTime: BehaviorSubject<Long> = BehaviorSubject.create()

    override val isCodeRequestAvailable: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(true).apply {
        nextCodeRequestWaitingTime
            .map{ it <= 0 }
            .distinctUntilChanged()
            .observeOn(ioScheduler)
            .subscribe(::onNext)
            .addTo(disposables)
    }

    init {
        recoverDataFromSharedPreferences()
        startSavingNextCodeRequestWaitingTime()
    }

    private fun startSavingNextCodeRequestWaitingTime(){
        nextCodeRequestWaitingTime
            .observeOn(ioScheduler)
            .subscribe(::saveNextCodeRequestWaitingTimeToSharedPreferences)
            .addTo(disposables)
    }

    override fun updateAuthorizationToken(token: String?): Completable {
        return Completable
            .create{ emitter ->
                saveTokenToSharedPreferences(token)
                affectTheExistenceOfAuthorizedUserComponent(token)
                emitter.onComplete()
            }
            .subscribeOn(ioScheduler)
    }

    override fun getRequiredCodeLength(): Single<Int> {
        return Single
            .just(codeLength)
            .subscribeOn(ioScheduler)
    }

    override fun saveRequiredCodeLength(length: Int): Completable {
        return Completable
            .create { emitter ->
                codeLength = length
                emitter.onComplete()
            }
            .subscribeOn(ioScheduler)
    }

    override fun saveNextCodeRequestRequiredWaitingTime(timeInSeconds: Long): Completable {
        return Completable
            .create{ emitter ->
                nextCodeRequestRequiredWaitingTime = timeInSeconds
                emitter.onComplete()
            }
            .subscribeOn(ioScheduler)

    }

    override fun getNextCodeRequestRequiredWaitingTime(): Single<Long> {
        return Single
            .just(nextCodeRequestRequiredWaitingTime)
            .subscribeOn(ioScheduler)
    }

    override fun startCodeRequestTimer() {
        startCodeRequestTimer(nextCodeRequestRequiredWaitingTime)
    }

    private fun recoverDataFromSharedPreferences(){
        val authorizationToken = getTokenFromSharedPreferences()
        affectTheExistenceOfAuthorizedUserComponent(authorizationToken)

        val nextCodeRequestWaitingTime = getNextCodeRequestWaitingTimeFromSharedPreferences()
        startCodeRequestTimer(nextCodeRequestWaitingTime)
    }

    private fun affectTheExistenceOfAuthorizedUserComponent(token: String?){
        if(token != null){
            createAuthorizedUserComponent(token)
        } else{
            destroyAuthorizedUserComponent()
        }
    }

    private fun saveTokenToSharedPreferences(token: String?){
        authorizationSharedPreferences.edit{
            putString(TOKEN_SHARED_PREFERENCES_KEY, token)
        }
    }

    private fun getTokenFromSharedPreferences(): String?{
        return authorizationSharedPreferences.getString(TOKEN_SHARED_PREFERENCES_KEY, null)
    }

    private fun createAuthorizedUserComponent(token: String){
        destroyAuthorizedUserComponent()
        authorizedUserComponent = authorizedUserComponentFactory.create(token)
        isUserLoggedInSubject.onNext(true)
    }

    private fun destroyAuthorizedUserComponent(){
        isUserLoggedInSubject.onNext(false)
        authorizedUserComponent = null
    }

    private fun saveNextCodeRequestWaitingTimeToSharedPreferences(leftTimeToWaitInSeconds: Long){
        val currentDate = GregorianCalendar.getInstance()
        val dateWhenCodeRequestIsAvailable = currentDate.timeInMillis + leftTimeToWaitInSeconds * 1000
        val timeZoneId = currentDate.timeZone.id

        authorizationSharedPreferences.edit{
            putLong(NEXT_CODE_REQUEST_DATE_SHARED_PREFERENCES_KEY, dateWhenCodeRequestIsAvailable)
            putString(NEXT_CODE_REQUEST_TIME_ZONE_SHARED_PREFERENCES_KEY, timeZoneId)
        }
    }

    private fun getNextCodeRequestWaitingTimeFromSharedPreferences(): Long{
        val currentDate = GregorianCalendar.getInstance()

        val nextCodeRequestDateInMillis = authorizationSharedPreferences.getLong(
            NEXT_CODE_REQUEST_DATE_SHARED_PREFERENCES_KEY,
            currentDate.timeInMillis
        )
        val timeZoneId = authorizationSharedPreferences.getString(
            NEXT_CODE_REQUEST_TIME_ZONE_SHARED_PREFERENCES_KEY,
            currentDate.timeZone.id
        )

        val nextCodeRequestDate =
            GregorianCalendar
                .getInstance(TimeZone.getTimeZone(timeZoneId)).apply{
                    timeInMillis = nextCodeRequestDateInMillis
                }

        val waitingTimeInSeconds = ((nextCodeRequestDate.timeInMillis - currentDate.timeInMillis) / 1000)
        return waitingTimeInSeconds.coerceIn(0..Long.MAX_VALUE)
    }

    private fun startCodeRequestTimer(time: Long){
        codeRequestTimer?.dispose()

        codeRequestTimer = Observable
            .intervalRange(
                0L,
                time + 1,
                0L,
                1L,
                TimeUnit.SECONDS
            )
            .map{ time - it }
            .subscribeOn(computationScheduler)
            .observeOn(ioScheduler)
            .subscribe(nextCodeRequestWaitingTime::onNext)
            .addTo(disposables)
    }
}