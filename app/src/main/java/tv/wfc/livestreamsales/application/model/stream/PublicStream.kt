package tv.wfc.livestreamsales.application.model.stream

import org.joda.time.DateTime

data class PublicStream(
    override val id: Long,
    override val streamerId: Long,
    override val title: String,
    override val description: String,
    override val startsAt: DateTime,
    override val endsAt: DateTime,
    override val imageUrl: String?,
    override val manifestUrl: String?
): IStream