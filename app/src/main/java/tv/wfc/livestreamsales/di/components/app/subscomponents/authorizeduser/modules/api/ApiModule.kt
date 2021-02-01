package tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.modules.api

import tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.modules.rest.qualifiers.AuthorizedApiProvider
import tv.wfc.livestreamsales.network.rest.IApiProvider
import tv.wfc.livestreamsales.network.rest.api.authorized.ILogOutApi
import dagger.Module
import dagger.Provides

@Module
class ApiModule {
    @Provides
    internal fun provideLogOutApi(
        @AuthorizedApiProvider
        authorizedApiProvider: IApiProvider
    ): ILogOutApi{
        return authorizedApiProvider.createApi(ILogOutApi::class.java)
    }
}