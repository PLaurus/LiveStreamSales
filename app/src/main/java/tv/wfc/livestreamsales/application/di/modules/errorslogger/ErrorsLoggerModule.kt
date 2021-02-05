package tv.wfc.livestreamsales.application.di.modules.errorslogger

import tv.wfc.livestreamsales.application.tools.errors.ApplicationErrorsLogger
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import tv.wfc.livestreamsales.application.di.scope.ApplicationScope
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