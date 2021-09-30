package tv.wfc.livestreamsales.application.model.streamchatmessage

import tv.wfc.livestreamsales.application.base.entity.domain.IDomainEntity

data class StreamChatMessage(
    val sender: Sender,
    val text: String
): IDomainEntity {
    data class Sender(
        val id: Long,
        val name: String?,
        val surname: String?
    ): IDomainEntity
}