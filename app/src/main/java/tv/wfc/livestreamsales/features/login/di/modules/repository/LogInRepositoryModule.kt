package tv.wfc.livestreamsales.features.login.di.modules.repository

import dagger.Binds
import dagger.Module
import tv.wfc.livestreamsales.features.login.di.scope.LogInFeatureScope
import tv.wfc.livestreamsales.features.login.repository.ILoginRepository
import tv.wfc.livestreamsales.features.login.repository.LoginRepository

@Module
abstract class LogInRepositoryModule {
    @LogInFeatureScope
    @Binds
    internal abstract fun provideLoginRepository(
        loginRepository: LoginRepository
    ): ILoginRepository
}