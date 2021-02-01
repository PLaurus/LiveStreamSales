package tv.wfc.livestreamsales.di.components.app.modules.utils

import tv.wfc.livestreamsales.utils.IStringResAnnotationProcessor
import tv.wfc.livestreamsales.utils.StringResAnnotationProcessor
import dagger.Binds
import dagger.Module

@Module
abstract class UtilsModule {
    @Binds
    internal abstract fun provideStringResAnnotationProcessor(
        stringResAnnotationProcessor: StringResAnnotationProcessor
    ): IStringResAnnotationProcessor
}