package com.example.livestreamsales.repository.authorization

import io.reactivex.rxjava3.core.Maybe

interface IAuthorizationRepository{
    fun sendVerificationCodeRequest(telephoneNumber: String): Maybe<Boolean>
}