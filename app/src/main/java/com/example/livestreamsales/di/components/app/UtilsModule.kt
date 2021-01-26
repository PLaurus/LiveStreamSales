package com.example.livestreamsales.di.components.app

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