package tv.wfc.livestreamsales.features.authorization.di.modules.repository

import dagger.Binds
import dagger.Module
import tv.wfc.livestreamsales.features.authorization.di.scope.AuthorizationFeatureScope
import tv.wfc.livestreamsales.features.authorization.repository.ILoginRepository
import tv.wfc.livestreamsales.features.authorization.repository.LoginRepository

@Module
abstract class LogInRepositoryModule {
    @AuthorizationFeatureScope
    @Binds
    internal abstract fun provideLoginRepository(
        loginRepository: LoginRepository
    ): ILoginRepository
}