package com.example.livestreamsales.authorization

import com.example.livestreamsales.di.components.authorizeduser.AuthorizedUserComponent
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable

interface IAuthorizationManager {
    var authorizedUserComponent: AuthorizedUserComponent?
    val token: Observable<String?>
    val isUserLoggedIn: Observable<Boolean>

    fun sendVerificationCodeRequest(telephoneNumber: String): Maybe<Boolean>
}