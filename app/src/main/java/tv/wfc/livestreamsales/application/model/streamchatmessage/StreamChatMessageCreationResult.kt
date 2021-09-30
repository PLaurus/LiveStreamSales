package tv.wfc.livestreamsales.application.model.streamchatmessage

import tv.wfc.livestreamsales.application.base.entity.domain.IDomainEntity

data class StreamChatMessageCreationResult(
    val isSent: Boolean,
    val errorMessage: String? = null
): IDomainEntity