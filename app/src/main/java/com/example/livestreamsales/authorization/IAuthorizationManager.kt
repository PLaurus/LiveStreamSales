package com.example.livestreamsales.authorization

import com.example.livestreamsales.di.components.authorizeduser.AuthorizedUserComponent
import com.example.livestreamsales.model.application.authorization.LogInResult
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface IAuthorizationManager {
    val authorizedUserComponent: AuthorizedUserComponent?
    val isUserLoggedIn: Observable<Boolean>
    val nextCodeRequestWaitingTime: Observable<Long>
    val isCodeRequestAvailable: Observable<Boolean>

    fun sendVerificationCodeRequest(telephoneNumber: String): Maybe<Boolean>
    fun getVerificationCodeLength(): Single<Int>
    fun logIn(phoneNumber: String, verificationCode: Int): Single<LogInResult>
    fun logOut(): Completable
}