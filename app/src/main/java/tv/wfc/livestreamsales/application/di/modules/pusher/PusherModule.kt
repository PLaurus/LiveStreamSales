package tv.wfc.livestreamsales.application.di.modules.pusher

import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import dagger.Binds
import dagger.Module
import dagger.Provides
import tv.wfc.livestreamsales.application.di.modules.pusher.qualifiers.PusherApiKey
import tv.wfc.livestreamsales.application.di.modules.pusher.qualifiers.PusherCluster
import tv.wfc.livestreamsales.application.pusherfacade.IPusherFacade
import tv.wfc.livestreamsales.application.pusherfacade.PusherFacade
import tv.wfc.livestreamsales.application.pusherfacade.PusherFacadeSimpleLogger

@Module
abstract class PusherModule {
    @Binds
    internal abstract fun bindIPusherFacade(
        pusherFacade: PusherFacade
    ): IPusherFacade

    @Binds
    internal abstract fun bindPusherFacadeILogger(
        logger: PusherFacadeSimpleLogger
    ): PusherFacade.ILogger

    companion object {
        @Provides
        internal fun providePusher(
            @PusherApiKey
            apiKey: String,
            pusherOptions: PusherOptions
        ) : Pusher {
            return Pusher(apiKey, pusherOptions)
        }

        @Provides
        @PusherApiKey
        internal fun providePusherApiKey() = "db935e1914a80d5559cc"

        @Provides
        internal fun providePusherOptions(
            @PusherCluster
            cluster: String
        ): PusherOptions {
            return PusherOptions()
                .setCluster(cluster)
        }

        @Provides
        @PusherCluster
        internal fun providePusherCluster() = "eu"
    }
}