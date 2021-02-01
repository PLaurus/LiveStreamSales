package tv.wfc.livestreamsales.di.components.app.modules.database

import android.content.Context
import android.content.SharedPreferences
import tv.wfc.livestreamsales.di.components.app.modules.database.qualifiers.ApplicationSharedPreferences
import tv.wfc.livestreamsales.di.components.app.modules.database.qualifiers.AuthorizationSharedPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class SharedPreferencesModule {
    companion object{
        private const val DEPENDENCY_NAME_AUTHORIZATION_FILE_NAME = "AUTHORIZATION_FILE_NAME"
        private const val DEPENDENCY_NAME_APPLICATION_SETTINGS_FILE_NAME = "APPLICATION_SETTINGS_FILE_NAME"
    }

    @Provides
    @Named(DEPENDENCY_NAME_AUTHORIZATION_FILE_NAME)
    internal fun provideAuthorizationSharedPreferencesFileName(): String{
        return "tv.wfc.livestreamsales.AUTHORIZATION_SHARED_PREFERENCES"
    }

    @Provides
    @Named(DEPENDENCY_NAME_APPLICATION_SETTINGS_FILE_NAME)
    internal fun provideApplicationSharedPreferencesFileName(): String {
        return "tv.wfc.livestreamsales.APPLICATION_SHARED_PREFERENCES"
    }

    @Provides
    @AuthorizationSharedPreferences
    internal fun provideAuthorizationSharedPreferences(
        context: Context,
        @Named(DEPENDENCY_NAME_AUTHORIZATION_FILE_NAME)
        authorizationSharedPreferencesFileName: String
    ): SharedPreferences{
        return context.getSharedPreferences(
            authorizationSharedPreferencesFileName,
            Context.MODE_PRIVATE
        )
    }

    @Provides
    @ApplicationSharedPreferences
    internal fun provideApplicationSharedPreferences(
        context: Context,
        @Named(DEPENDENCY_NAME_APPLICATION_SETTINGS_FILE_NAME)
        applicationSharedPreferencesFileName: String
    ): SharedPreferences{
        return context.getSharedPreferences(
            applicationSharedPreferencesFileName,
            Context.MODE_PRIVATE
        )
    }
}