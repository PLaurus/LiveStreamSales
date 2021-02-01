package com.example.livestreamsales.repository.authorization

import com.example.livestreamsales.di.components.authorizeduser.AuthorizedUserComponent
import com.example.livestreamsales.model.application.phonenumberconfirmation.PhoneNumberConfirmationResult
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface IAuthorizationRepository{
    val isUserLoggedIn: Observable<Boolean>
    val authorizedUserComponent: AuthorizedUserComponent?
    val nextCodeRequestWaitingTime: Observable<Long>
    val isCodeRequestAvailable: Observable<Boolean>
    fun sendConfirmationCodeRequest(phoneNumber: String): Single<Boolean>
    fun getRequiredCodeLength(): Single<Int>
    fun getNextCodeRequestRequiredWaitingTime(): Single<Long>
    fun confirmPhoneNumber(phoneNumber: String, confirmationCode: Int): Single<PhoneNumberConfirmationResult>
    fun logOut(): Completable
}