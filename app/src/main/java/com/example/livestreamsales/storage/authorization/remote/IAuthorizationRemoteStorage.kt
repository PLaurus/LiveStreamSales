package com.example.livestreamsales.storage.authorization.remote

import com.example.livestreamsales.model.application.authorization.PhoneNumberVerificationResult
import io.reactivex.rxjava3.core.Maybe

interface IAuthorizationRemoteStorage {
    fun sendVerificationCodeRequest(telephoneNumber: String): Maybe<Boolean>
    fun getCodeLength(): Maybe<Int>
    fun verifyPhoneNumber(phoneNumber: String, verificationCode: Int): Maybe<PhoneNumberVerificationResult>
}