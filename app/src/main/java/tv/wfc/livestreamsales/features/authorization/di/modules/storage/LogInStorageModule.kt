package tv.wfc.livestreamsales.features.authorization.di.modules.storage

import dagger.Binds
import dagger.Module
import tv.wfc.livestreamsales.features.authorization.di.modules.storage.qualifiers.LoginLocalStorage
import tv.wfc.livestreamsales.features.authorization.storage.ILoginStorage

@Module
abstract class LogInStorageModule {
    @Binds
    @LoginLocalStorage
    internal abstract fun provideLoginLocalStorage(
        loginLocalStorage: tv.wfc.livestreamsales.features.authorization.storage.local.LoginLocalStorage
    ): ILoginStorage
}