package tv.wfc.livestreamsales.application.tools.errors

interface IApplicationErrorsLogger {
    fun logError(throwable: Throwable)
}