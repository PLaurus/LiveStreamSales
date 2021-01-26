package com.example.livestreamsales.di.components.app.modules.errorslogger

import com.example.livestreamsales.application.errors.ApplicationErrorsLogger
import com.example.livestreamsales.application.errors.IApplicationErrorsLogger
import com.example.livestreamsales.di.scopes.ApplicationScope
import dagger.Binds
import dagger.Module

@Module
abstract class ErrorsLoggerModule {
    @ApplicationScope
    @Binds
    internal abstract fun provideApplicationErrorsLogger(
        applicationErrorsLogger: ApplicationErrorsLogger
    ): IApplicationErrorsLogger
}