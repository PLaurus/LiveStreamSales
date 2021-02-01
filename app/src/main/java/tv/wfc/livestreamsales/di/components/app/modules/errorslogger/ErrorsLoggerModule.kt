package tv.wfc.livestreamsales.di.components.app.modules.errorslogger

import tv.wfc.livestreamsales.application.errors.ApplicationErrorsLogger
import tv.wfc.livestreamsales.application.errors.IApplicationErrorsLogger
import tv.wfc.livestreamsales.di.scopes.ApplicationScope
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