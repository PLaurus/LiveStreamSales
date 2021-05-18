package tv.wfc.livestreamsales.features.livebroadcast.di.modules.exoplayer

import android.content.Context
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.RenderersFactory
import com.google.android.exoplayer2.ext.cronet.CronetDataSource
import com.google.android.exoplayer2.ext.cronet.CronetEngineWrapper
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.MediaSourceFactory
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.util.ErrorMessageProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import tv.wfc.livestreamsales.features.livebroadcast.di.modules.exoplayer.qualifiers.PreferExtensionRenderer
import tv.wfc.livestreamsales.features.livebroadcast.di.modules.exoplayer.qualifiers.UseCronetForNetworking
import tv.wfc.livestreamsales.features.livebroadcast.di.modules.exoplayer.qualifiers.UseExtensionRenderers
import tv.wfc.livestreamsales.features.livebroadcast.exoplayer.PlayerErrorMessageProvider
import java.net.CookieHandler
import java.net.CookieManager
import java.net.CookiePolicy
import java.util.concurrent.Executors

@Module
abstract class ExoPlayerModule {
    @Binds
    internal abstract fun provideErrorMessageProvider(
        playerErrorMessageProvider: PlayerErrorMessageProvider
    ): ErrorMessageProvider<ExoPlaybackException>

    companion object{
        @Provides
        @UseExtensionRenderers
        internal fun useExtensionRenderers() = false

        @Provides
        @PreferExtensionRenderer
        internal fun preferExtensionRenderer() = false

        /**
         * Whether the application uses Cronet for networking. Note that Cronet does not provide
         * automatic support for cookies (https://github.com/google/ExoPlayer/issues/5975).
         *
         * If set to false, the platform's default network stack is used with a [CookieManager]
         * configured in [.getHttpDataSourceFactory].
         */
        @Provides
        @UseCronetForNetworking
        internal fun useCronetForNetworking() = true

        @Provides
        internal fun provideRenderersFactory(
            context: Context,
            @UseExtensionRenderers
            useExtensionRenderers: Boolean,
            @PreferExtensionRenderer
            preferExtensionRenderer: Boolean
        ): RenderersFactory{
            val extensionRendererMode = if(useExtensionRenderers){
                if(preferExtensionRenderer) {
                    DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER
                } else{
                    DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON
                }
            } else{
                DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF
            }

            return DefaultRenderersFactory(context).setExtensionRendererMode(extensionRendererMode)
        }

        @Provides
        internal fun provideMediaSourceFactory(
            dataSourceFactory: DataSource.Factory
        ): MediaSourceFactory{
            return DefaultMediaSourceFactory(dataSourceFactory)
        }

        @Provides
        internal fun provideTrackSelector(
            context: Context
        ): TrackSelector{
            return DefaultTrackSelector(context)
        }

        @Provides
        internal fun provideDataSourceFactory(
            context: Context,
            httpDataSourceFactory: HttpDataSource.Factory,
        ): DataSource.Factory{
            return DefaultDataSourceFactory(context, httpDataSourceFactory)
        }

        @Provides
        internal fun provideHttpDataSourceFactory(
            context: Context,
            @UseCronetForNetworking
            useCronetForNetworking: Boolean
        ): HttpDataSource.Factory{
             return if (useCronetForNetworking) {
                 val cronetEngineWrapper = CronetEngineWrapper(context, null, false)

                 CronetDataSource.Factory(cronetEngineWrapper, Executors.newSingleThreadExecutor())
            } else {
                val cookieManager = CookieManager()
                cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER)
                CookieHandler.setDefault(cookieManager)
                DefaultHttpDataSource.Factory()
            }
        }
    }
}