package com.example.livestreamsales.repository.greeting

import com.example.livestreamsales.di.components.splash.modules.greeting.qualifiers.GreetingLocalStorage
import com.example.livestreamsales.storage.greeting.IGreetingStorage
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GreetingRepository @Inject constructor(
    @GreetingLocalStorage
    private val greetingLocalStorage: IGreetingStorage
): IGreetingRepository {
    override fun getIsGreetingShown(): Single<Boolean> {
        return greetingLocalStorage.getIsGreetingShown()
    }

    override fun saveIsGreetingShown(isShown: Boolean): Completable {
        return greetingLocalStorage.saveIsGreetingShown(isShown)
    }
}