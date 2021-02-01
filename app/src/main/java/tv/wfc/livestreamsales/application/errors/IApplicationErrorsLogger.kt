package tv.wfc.livestreamsales.application.errors

interface IApplicationErrorsLogger {
    fun logError(throwable: Throwable)
}