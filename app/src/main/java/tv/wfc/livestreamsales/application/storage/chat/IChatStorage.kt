package tv.wfc.livestreamsales.application.storage.chat

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import tv.wfc.livestreamsales.application.model.chat.ChatMessage

interface IChatStorage {
    fun sendMessage(message: String): Completable
    fun getChatObservable(): Observable<ChatMessage>
}