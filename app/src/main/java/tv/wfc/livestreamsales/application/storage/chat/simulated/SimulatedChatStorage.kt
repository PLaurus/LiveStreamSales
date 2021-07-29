package tv.wfc.livestreamsales.application.storage.chat.simulated

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.PublishSubject
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.application.model.chat.ChatMessage
import tv.wfc.livestreamsales.application.repository.userpersonalinformation.IUserPersonalInformationRepository
import tv.wfc.livestreamsales.application.storage.chat.IChatStorage
import javax.inject.Inject

class SimulatedChatStorage @Inject constructor(
    private val userPersonalInformationRepository: IUserPersonalInformationRepository,
    @IoScheduler
    private val ioScheduler: Scheduler
): IChatStorage {
    private val chatMessages = PublishSubject.create<ChatMessage>()

    override fun sendMessage(message: String): Completable {
        return Completable.create { emitter ->
            val disposable = createMessageFromCurrentUser(message)
                .observeOn(ioScheduler)
                .subscribeBy(
                    onSuccess = {
                        chatMessages.onNext(it)
                        emitter.onComplete()
                    },
                    onError = emitter::onError
                )

            emitter.setDisposable(disposable)
        }
    }

    override fun getChatObservable(): Observable<ChatMessage> = chatMessages

    private fun createMessageFromCurrentUser(message: String): Single<ChatMessage> {
        return userPersonalInformationRepository
            .getUserPersonalInformation()
            .lastOrError()
            .map { "${it.name} ${it.surname}" }
            .map { sender -> ChatMessage(sender, message) }
    }
}