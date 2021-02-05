package tv.wfc.livestreamsales.application.di.modules.restapi

import dagger.Module
import dagger.Provides
import tv.wfc.livestreamsales.features.rest.IApiProvider
import tv.wfc.livestreamsales.features.rest.api.notauthorized.ILogInApi

@Module
class NotAuthorizedRestApiModule {
    @Provides
    internal fun provideLogInApi(apiProvider: IApiProvider): ILogInApi {
        return apiProvider.createApi(ILogInApi::class.java)
    }
}