package com.example.livestreamsales.repository.authorization

import com.example.livestreamsales.di.components.authorizeduser.AuthorizedUserComponent
import com.example.livestreamsales.model.application.phoneconfirmation.PhoneConfirmationResult
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface IAuthorizationRepository{
    val isUserLoggedIn: Observable<Boolean>
    val authorizedUserComponent: AuthorizedUserComponent?
    val nextCodeRequestWaitingTime: Observable<Long>
    val isCodeRequestAvailable: Observable<Boolean>
    fun sendVerificationCodeRequest(telephoneNumber: String): Single<Boolean>
    fun getRequiredCodeLength(): Single<Int>
    fun getNextCodeRequestRequiredWaitingTime(): Single<Long>
    fun confirmPhone(phoneNumber: String, verificationCode: Int): Single<PhoneConfirmationResult>
    fun logOut(): Completable
}