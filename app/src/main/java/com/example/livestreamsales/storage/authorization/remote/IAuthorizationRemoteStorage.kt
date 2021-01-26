package com.example.livestreamsales.storage.authorization.remote

import com.example.livestreamsales.model.application.phoneconfirmation.PhoneConfirmationResult
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single

interface IAuthorizationRemoteStorage {
    fun sendVerificationCodeRequest(telephoneNumber: String): Single<Boolean>
    fun getRequiredCodeLength(): Maybe<Int>
    fun getNextCodeRequestRequiredWaitingTime(): Maybe<Long>
    fun confirmPhone(phoneNumber: String, verificationCode: Int): Single<PhoneConfirmationResult>
}