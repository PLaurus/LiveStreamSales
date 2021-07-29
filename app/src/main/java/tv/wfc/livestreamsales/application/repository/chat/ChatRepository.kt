package tv.wfc.livestreamsales.application.repository.chat

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import tv.wfc.livestreamsales.application.model.chat.ChatMessage
import tv.wfc.livestreamsales.application.storage.chat.IChatStorage
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val chatStorage: IChatStorage
): IChatRepository {
    override fun sendMessage(message: String): Completable {
        return chatStorage.sendMessage(message)
    }

    override fun getChatObservable(): Observable<ChatMessage> {
        return chatStorage.getChatObservable()
    }
}