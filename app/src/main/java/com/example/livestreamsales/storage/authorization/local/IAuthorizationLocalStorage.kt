package com.example.livestreamsales.storage.authorization.local

import com.example.livestreamsales.di.components.app.subscomponents.authorizeduser.AuthorizedUserComponent
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface IAuthorizationLocalStorage {
    val isUserLoggedIn: Observable<Boolean>
    val authorizedUserComponent: AuthorizedUserComponent?
    val nextCodeRequestWaitingTime: Observable<Long>
    val isCodeRequestAvailable: Observable<Boolean>
    fun updateAuthorizationToken(token: String?): Completable
    fun getRequiredCodeLength(): Single<Int>
    fun saveRequiredCodeLength(length: Int): Completable
    fun saveNextCodeRequestRequiredWaitingTime(timeInSeconds: Long): Completable
    fun getNextCodeRequestRequiredWaitingTime(): Single<Long>
    fun startCodeRequestTimer()
}