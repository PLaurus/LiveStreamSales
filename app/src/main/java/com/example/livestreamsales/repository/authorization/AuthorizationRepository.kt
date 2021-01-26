package com.example.livestreamsales.repository.authorization

import com.example.livestreamsales.application.errors.IApplicationErrorsLogger
import com.example.livestreamsales.di.components.app.modules.reactivex.qualifiers.MainThreadScheduler
import com.example.livestreamsales.model.application.authorization.PhoneNumberVerificationResult
import com.example.livestreamsales.storage.authorization.local.IAuthorizationLocalStorage
import com.example.livestreamsales.storage.authorization.remote.IAuthorizationRemoteStorage
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import javax.inject.Inject

class AuthorizationRepository @Inject constructor(
    private val authorizationRemoteStorage: IAuthorizationRemoteStorage,
    private val authorizationLocalStorage: IAuthorizationLocalStorage,
    @MainThreadScheduler
    private val mainThreadScheduler: Scheduler,
    private val applicationErrorsLogger: IApplicationErrorsLogger
): IAuthorizationRepository {
    private val disposables = CompositeDisposable()

    override fun sendVerificationCodeRequest(telephoneNumber: String): Maybe<Boolean> {
        return authorizationRemoteStorage.sendVerificationCodeRequest(telephoneNumber)
    }

    override fun getVerificationCodeLength(): Single<Int> {
        return getAndSaveCodeLengthFromRemote()
            .switchIfEmpty(authorizationLocalStorage.getCodeLength())
    }

    override fun verifyPhoneNumber(phoneNumber: String, verificationCode: Int): Maybe<PhoneNumberVerificationResult>{
        return authorizationRemoteStorage.verifyPhoneNumber(phoneNumber, verificationCode)
    }

    private fun getAndSaveCodeLengthFromRemote(): Maybe<Int>{
        return authorizationRemoteStorage.getCodeLength()
            .doOnSuccess {
                saveCodeLengthLocally(it)
            }
    }

    private fun saveCodeLengthLocally(length: Int){
        authorizationLocalStorage.setCodeLength(length)
            .observeOn(mainThreadScheduler)
            .subscribeBy(onError = applicationErrorsLogger::logError)
            .addTo(disposables)
    }
}