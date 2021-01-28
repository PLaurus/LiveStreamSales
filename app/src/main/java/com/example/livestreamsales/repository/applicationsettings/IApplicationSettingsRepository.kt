package com.example.livestreamsales.repository.applicationsettings

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface IApplicationSettingsRepository {
    fun getIsGreetingShown(): Single<Boolean>
    fun saveIsGreetingShown(isShown: Boolean): Completable
}