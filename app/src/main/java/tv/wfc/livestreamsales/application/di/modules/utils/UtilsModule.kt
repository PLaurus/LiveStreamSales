package tv.wfc.livestreamsales.application.di.modules.utils

import tv.wfc.livestreamsales.application.tools.stringresannotation.IStringResAnnotationProcessor
import tv.wfc.livestreamsales.application.tools.stringresannotation.StringResAnnotationProcessor
import dagger.Binds
import dagger.Module

@Module
abstract class UtilsModule {
    @Binds
    internal abstract fun provideStringResAnnotationProcessor(
        stringResAnnotationProcessor: StringResAnnotationProcessor
    ): IStringResAnnotationProcessor
}