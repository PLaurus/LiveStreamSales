package tv.wfc.livestreamsales.application.pusherfacade

import android.util.Log
import javax.inject.Inject

class PusherFacadeSimpleLogger @Inject constructor(

): PusherFacade.ILogger {
    private val logTag = this::class.simpleName

    override fun logReceivedData(data: String) {
        val formattedData = "Received from pusher: $data"
        Log.i(logTag, formattedData)
    }
}