package tv.wfc.livestreamsales.application.pusherfacade

import com.pusher.client.Pusher
import com.pusher.client.channel.SubscriptionEventListener
import com.pusher.client.connection.ConnectionEventListener
import com.pusher.client.connection.ConnectionState
import com.pusher.client.connection.ConnectionStateChange
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import tv.wfc.livestreamsales.application.model.exception.storage.ReceivedDataWithWrongFormatException
import tv.wfc.livestreamsales.application.tools.serialization.ISerializationBehavior
import javax.inject.Inject
import kotlin.reflect.KClass

class PusherFacade @Inject constructor(
    private val pusherClient: Pusher,
    private val serializationBehavior: ISerializationBehavior,
    private val logger: ILogger? = null
): IPusherFacade {
    private val connection = pusherClient.connection

    override fun <T: Any> subscribeOnData(
        channelName: String,
        channelEventName: String,
        expectedClass: KClass<T>
    ): Observable<T> {
        return Observable.create { emitter ->
            val eventsChannel = pusherClient.subscribe(channelName)
            val channelEventListener = createChannelEventProcessor(emitter, expectedClass)
            eventsChannel?.bind(channelEventName, channelEventListener)

            val connectionEventListener = createConnectionEventListener(emitter)
            connection.bind(ConnectionState.ALL, connectionEventListener)

            pusherClient.connect()

            emitter.setCancellable {
                connection.unbind(ConnectionState.ALL, connectionEventListener)

                eventsChannel?.unbind(channelEventName, channelEventListener)
                pusherClient.unsubscribe(eventsChannel.name)

                pusherClient.disconnect()
            }
        }
    }

    private fun <T: Any> createChannelEventProcessor(
        emitter: ObservableEmitter<T>,
        expectedClass: KClass<T>
    ) = SubscriptionEventListener { pusherEvent ->
        val receivedData = pusherEvent.data
        logger?.logReceivedData(receivedData)
        val newData = serializationBehavior.deserialize(receivedData, expectedClass)
        newData?.let(emitter::onNext) ?: emitter.onError(ReceivedDataWithWrongFormatException())
    }

    private fun <T: Any> createConnectionEventListener(emitter: ObservableEmitter<T>) = object: ConnectionEventListener {
        override fun onConnectionStateChange(change: ConnectionStateChange?) {
            val newConnectionState = change?.currentState ?: return

            if(newConnectionState == ConnectionState.DISCONNECTED) pusherClient.connect()
        }

        override fun onError(message: String?, code: String?, e: Exception?) {
            e?.let(emitter::onError)
        }
    }

    interface ILogger {
        fun logReceivedData(data: String)
    }
}