package com.example.livestreamsales.repository.authorization

import com.example.livestreamsales.model.application.authorization.PhoneNumberVerificationResult
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single

interface IAuthorizationRepository{
    fun sendVerificationCodeRequest(telephoneNumber: String): Maybe<Boolean>
    fun getVerificationCodeLength(): Single<Int>
    fun verifyPhoneNumber(phoneNumber: String, verificationCode: Int): Maybe<PhoneNumberVerificationResult>
}