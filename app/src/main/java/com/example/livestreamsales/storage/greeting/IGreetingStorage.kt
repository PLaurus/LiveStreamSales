package com.example.livestreamsales.storage.greeting

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface IGreetingStorage {
    fun getIsGreetingShown(): Single<Boolean>
    fun saveIsGreetingShown(isShown: Boolean): Completable
}