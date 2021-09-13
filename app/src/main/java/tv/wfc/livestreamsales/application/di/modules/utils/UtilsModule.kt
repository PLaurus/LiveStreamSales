package tv.wfc.livestreamsales.application.di.modules.utils

import tv.wfc.livestreamsales.application.tools.stringresannotation.IStringResAnnotationProcessor
import tv.wfc.livestreamsales.application.tools.stringresannotation.StringResAnnotationProcessor
import dagger.Binds
import dagger.Module
import tv.wfc.livestreamsales.application.tools.camera.ICameraPreviewSizeReceivingBehavior
import tv.wfc.livestreamsales.application.tools.camera.camera1.Camera1PreviewSizeReceivingBehavior

@Module
abstract class UtilsModule {
    @Binds
    internal abstract fun provideStringResAnnotationProcessor(
        stringResAnnotationProcessor: StringResAnnotationProcessor
    ): IStringResAnnotationProcessor

    @Binds
    internal abstract fun bindICameraPreviewSizeReceivingBehavior(
        behavior: Camera1PreviewSizeReceivingBehavior
    ): ICameraPreviewSizeReceivingBehavior
}