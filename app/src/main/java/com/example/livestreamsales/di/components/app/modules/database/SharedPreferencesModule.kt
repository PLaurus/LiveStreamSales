package com.example.livestreamsales.di.components.app.modules.database

import android.content.Context
import android.content.SharedPreferences
import com.example.livestreamsales.di.components.app.modules.database.qualifiers.AuthorizationSharedPreferences
import com.example.livestreamsales.di.scopes.ApplicationScope
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class SharedPreferencesModule {
    companion object{
        private const val DEPENDENCY_NAME_AUTHORIZATION_FILE_NAME = "AUTHORIZATION_FILE_KEY"
    }

    @Provides
    @Named(DEPENDENCY_NAME_AUTHORIZATION_FILE_NAME)
    internal fun provideAuthorizationPreferencesFileName(): String = "tv.wfc.livestreamsales.AUTHORIZATION_PREFERENCES_FILE_NAME"

    @ApplicationScope
    @Provides
    @AuthorizationSharedPreferences
    internal fun provideAuthorizationSharedPreferences(
        context: Context,
        @Named(DEPENDENCY_NAME_AUTHORIZATION_FILE_NAME)
        authorizationPreferencesFileKey: String
    ): SharedPreferences{
        return context.getSharedPreferences(authorizationPreferencesFileKey, Context.MODE_PRIVATE)
    }
}