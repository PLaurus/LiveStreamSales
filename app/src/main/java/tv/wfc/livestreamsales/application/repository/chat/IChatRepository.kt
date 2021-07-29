package tv.wfc.livestreamsales.application.repository.chat

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import tv.wfc.livestreamsales.application.model.chat.ChatMessage

interface IChatRepository {
    fun sendMessage(message: String): Completable
    fun getChatObservable(): Observable<ChatMessage>
}