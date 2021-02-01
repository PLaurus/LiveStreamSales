package com.example.livestreamsales.storage.authorization.remote

import com.example.livestreamsales.model.application.phonenumberconfirmation.PhoneNumberConfirmationResult
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single

interface IAuthorizationRemoteStorage {
    fun sendConfirmationCodeRequest(phoneNumber: String): Single<Boolean>
    fun getRequiredCodeLength(): Maybe<Int>
    fun getNextCodeRequestRequiredWaitingTime(): Maybe<Long>
    fun confirmPhoneNumber(phoneNumber: String, confirmationCode: Int): Single<PhoneNumberConfirmationResult>
}