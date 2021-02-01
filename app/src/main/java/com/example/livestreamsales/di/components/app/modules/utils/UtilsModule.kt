package com.example.livestreamsales.di.components.app.modules.utils

import com.example.livestreamsales.utils.IStringResAnnotationProcessor
import com.example.livestreamsales.utils.StringResAnnotationProcessor
import dagger.Binds
import dagger.Module

@Module
abstract class UtilsModule {
    @Binds
    internal abstract fun provideStringResAnnotationProcessor(
        stringResAnnotationProcessor: StringResAnnotationProcessor
    ): IStringResAnnotationProcessor
}