package tv.wfc.livestreamsales.application.di.modules.utils

import com.laurus.p.tools.camera.ICameraResolutionsProvider
import com.laurus.p.tools.camera.camera1.Camera1ResolutionsProvider
import dagger.Binds
import dagger.Module
import tv.wfc.livestreamsales.application.tools.stringresannotation.IStringResAnnotationProcessor
import tv.wfc.livestreamsales.application.tools.stringresannotation.StringResAnnotationProcessor

@Module
abstract class UtilsModule {
    @Binds
    internal abstract fun provideStringResAnnotationProcessor(
        stringResAnnotationProcessor: StringResAnnotationProcessor
    ): IStringResAnnotationProcessor

    @Binds
    internal abstract fun bindICameraResolutionsProvider(
        behavior: Camera1ResolutionsProvider
    ): ICameraResolutionsProvider
}