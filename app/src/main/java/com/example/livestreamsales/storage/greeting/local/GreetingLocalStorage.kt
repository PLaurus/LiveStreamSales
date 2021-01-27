package com.example.livestreamsales.storage.greeting.local

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.livestreamsales.di.components.app.modules.database.qualifiers.GreetingSharedPreferences
import com.example.livestreamsales.di.components.app.modules.reactivex.qualifiers.IoScheduler
import com.example.livestreamsales.storage.greeting.IGreetingStorage
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GreetingLocalStorage @Inject constructor(
    @GreetingSharedPreferences
    private val greetingSharedPreferences: SharedPreferences,
    @IoScheduler
    private val ioScheduler: Scheduler
): IGreetingStorage {
    companion object{
        private const val IS_GREETING_SHOWN_KEY = "is_greeting_shown"
    }

    /**
     * By default operates on IO Scheduler
     */
    override fun getIsGreetingShown(): Single<Boolean> {
        val isGreetingShown = getIsGreetingShownFromSharedPreferences()
        return Single
            .just(isGreetingShown)
            .subscribeOn(ioScheduler)
    }

    /**
     * By default operates on IO Scheduler
     */
    override fun saveIsGreetingShown(isShown: Boolean): Completable {
        return Completable
            .create{ emitter ->
                saveIsGreetingShownToSharedPreferences(isShown)
                emitter.onComplete()
            }
            .subscribeOn(ioScheduler)
    }

    private fun getIsGreetingShownFromSharedPreferences(): Boolean{
        return greetingSharedPreferences.getBoolean(IS_GREETING_SHOWN_KEY, false)
    }

    private fun saveIsGreetingShownToSharedPreferences(isShown: Boolean){
        greetingSharedPreferences.edit{
            putBoolean(IS_GREETING_SHOWN_KEY, isShown)
        }
    }
}