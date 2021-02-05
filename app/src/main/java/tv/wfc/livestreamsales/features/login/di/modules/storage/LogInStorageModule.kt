package tv.wfc.livestreamsales.features.login.di.modules.storage

import dagger.Binds
import dagger.Module
import tv.wfc.livestreamsales.features.login.di.modules.storage.qualifiers.LoginLocalStorage
import tv.wfc.livestreamsales.features.login.storage.ILoginStorage

@Module
abstract class LogInStorageModule {
    @Binds
    @LoginLocalStorage
    internal abstract fun provideLoginLocalStorage(
        loginLocalStorage: tv.wfc.livestreamsales.features.login.storage.local.LoginLocalStorage
    ): ILoginStorage
}