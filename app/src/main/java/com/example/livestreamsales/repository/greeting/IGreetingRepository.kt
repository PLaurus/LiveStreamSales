package com.example.livestreamsales.repository.greeting

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface IGreetingRepository {
    fun getIsGreetingShown(): Single<Boolean>
    fun saveIsGreetingShown(isShown: Boolean): Completable
}