package com.example.livestreamsales.di.components.app

import android.content.Context
import android.content.SharedPreferences
import com.example.livestreamsales.di.scopes.ApplicationScope
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class SharedPreferencesModule {

    companion object{
        private const val DEPENDENCY_NAME_AUTHORIZATION_FILE_KEY = "AUTHORIZATION_FILE_KEY"
        internal const val DEPENDENCY_NAME_AUTHORIZATION_PREFERENCES = "AUTHORIZATION_PREFERENCES"
    }

    @Provides
    @Named(DEPENDENCY_NAME_AUTHORIZATION_FILE_KEY)
    fun provideAuthorizationPreferencesFileKey(): String = "tv.wfc.livestreamsales.AUTHORIZATION_PREFERENCES_FILE_KEY"

    @ApplicationScope
    @Provides
    @Named(DEPENDENCY_NAME_AUTHORIZATION_PREFERENCES)
    internal fun provideAuthorizationSharedPreferences(
        context: Context,
        @Named(DEPENDENCY_NAME_AUTHORIZATION_FILE_KEY)
        authorizationPreferencesFileKey: String
    ): SharedPreferences{
        return context.getSharedPreferences(authorizationPreferencesFileKey, Context.MODE_PRIVATE)
    }
}