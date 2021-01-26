package com.example.livestreamsales.storage.authorization.local

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface IAuthorizationLocalStorage {
    fun getCodeLength(): Single<Int>
    fun setCodeLength(length: Int): Completable
}