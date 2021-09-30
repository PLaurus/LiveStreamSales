package tv.wfc.livestreamsales.application.pusherfacade

import io.reactivex.rxjava3.core.Observable
import kotlin.reflect.KClass

interface IPusherFacade {
    fun <T: Any> subscribeOnData(
        channelName: String,
        channelEventName: String,
        expectedClass: KClass<T>
    ): Observable<T>
}