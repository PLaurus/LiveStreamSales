package com.example.livestreamsales.di.components.app.modules.database

import android.content.Context
import android.content.SharedPreferences
import com.example.livestreamsales.di.components.app.modules.database.qualifiers.AuthorizationSharedPreferences
import com.example.livestreamsales.di.components.app.modules.database.qualifiers.GreetingSharedPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class SharedPreferencesModule {
    companion object{
        private const val DEPENDENCY_NAME_AUTHORIZATION_FILE_NAME = "AUTHORIZATION_FILE_NAME"
        private const val DEPENDENCY_NAME_GREETING_FILE_NAME = "GREETING_FILE_NAME"
    }

    @Provides
    @Named(DEPENDENCY_NAME_AUTHORIZATION_FILE_NAME)
    internal fun provideAuthorizationSharedPreferencesFileName(): String = "tv.wfc.livestreamsales.AUTHORIZATION_SHARED_PREFERENCES"

    @Provides
    @Named(DEPENDENCY_NAME_GREETING_FILE_NAME)
    internal fun provideGreetingSharedPreferencesFileName(): String = "tv.wfc.livestreamsales.GREETING_SHARED_PREFERENCES"

    @Provides
    @AuthorizationSharedPreferences
    internal fun provideAuthorizationSharedPreferences(
        context: Context,
        @Named(DEPENDENCY_NAME_AUTHORIZATION_FILE_NAME)
        authorizationSharedPreferencesFileName: String
    ): SharedPreferences{
        return context.getSharedPreferences(authorizationSharedPreferencesFileName, Context.MODE_PRIVATE)
    }

    @Provides
    @GreetingSharedPreferences
    internal fun provideGreetingSharedPreferences(
        context: Context,
        @Named(DEPENDENCY_NAME_GREETING_FILE_NAME)
        greetingSharedPreferencesFileName: String
    ): SharedPreferences{
        return context.getSharedPreferences(greetingSharedPreferencesFileName, Context.MODE_PRIVATE)
    }
}