package tv.wfc.livestreamsales.application.di.modules.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import tv.wfc.livestreamsales.application.di.modules.sharedpreferences.qualifiers.*

@Module
class SharedPreferencesModule {
    @Provides
    @AuthorizationSharedPreferencesFileName
    internal fun provideAuthorizationSharedPreferencesFileName(): String{
        return "tv.wfc.livestreamsales.AUTHORIZATION_SHARED_PREFERENCES"
    }

    @Provides
    @ApplicationSharedPreferencesFileName
    internal fun provideApplicationSharedPreferencesFileName(): String {
        return "tv.wfc.livestreamsales.APPLICATION_SHARED_PREFERENCES"
    }

    @Provides
    @LiveBroadcastingSharedPreferencesFileName
    internal fun provideLiveBroadcastingSharedPreferencesFileName(): String{
        return "tv.wfc.livestreamsales.LIVE_BROADCASTING_SHARED_PREFERENCES"
    }

    @Provides
    @AuthorizationSharedPreferences
    internal fun provideAuthorizationSharedPreferences(
        context: Context,
        @AuthorizationSharedPreferencesFileName
        fileName: String
    ): SharedPreferences{
        return context.getSharedPreferences(
            fileName,
            Context.MODE_PRIVATE
        )
    }

    @Provides
    @ApplicationSharedPreferences
    internal fun provideApplicationSharedPreferences(
        context: Context,
        @ApplicationSharedPreferencesFileName
        fileName: String
    ): SharedPreferences{
        return context.getSharedPreferences(
            fileName,
            Context.MODE_PRIVATE
        )
    }

    @Provides
    @LiveBroadcastingSharedPreferences
    internal fun provideLiveBroadcastingSharedPreferences(
        context: Context,
        @LiveBroadcastingSharedPreferencesFileName
        fileName: String
    ): SharedPreferences{
        return context.getSharedPreferences(
            fileName,
            Context.MODE_PRIVATE
        )
    }
}