package tv.wfc.livestreamsales.features.authorizeduser.di.modules.api

import tv.wfc.livestreamsales.features.authorizeduser.di.modules.rest.qualifiers.AuthorizedApiProvider
import tv.wfc.livestreamsales.features.rest.IApiProvider
import tv.wfc.livestreamsales.features.rest.api.authorized.ILogOutApi
import dagger.Module
import dagger.Provides

@Module
class ApiModule {
    @Provides
    internal fun provideLogOutApi(
        @AuthorizedApiProvider
        authorizedApiProvider: IApiProvider
    ): ILogOutApi {
        return authorizedApiProvider.createApi(ILogOutApi::class.java)
    }
}