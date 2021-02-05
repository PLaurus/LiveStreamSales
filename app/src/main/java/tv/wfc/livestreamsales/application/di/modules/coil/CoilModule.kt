package tv.wfc.livestreamsales.application.di.modules.coil

import android.content.Context
import coil.ImageLoader
import dagger.Module
import dagger.Provides

@Module
class CoilModule {
    @Provides
    internal fun provideImageLoader(
        context: Context
    ): ImageLoader{
        return ImageLoader.Builder(context)
            .availableMemoryPercentage(0.24)
            .crossfade(true)
            .build()
    }
}