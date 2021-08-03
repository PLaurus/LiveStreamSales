package tv.wfc.livestreamsales.application.model.broadcastinformation

import org.joda.time.DateTime

data class Broadcast(
    val id: Long,
    val title: String,
    val description: String,
    val startsAt: DateTime,
    val endsAt: DateTime,
    val imageUrl: String? = null,
    val manifestUrl: String? = null,
    val viewersCount: Int? = null
)