package tv.wfc.livestreamsales.features.mystreams.model

sealed class NextDestination {
    /**
     * Marker that is used to notify about closing current destination.
     */
    object Close: NextDestination()

    /**
     * Stream creation destination.
     */
    object StreamCreation: NextDestination()

    /**
     * Stream editing destination.
     * @param streamId Identifies a stream which user going to edit.
     */
    data class StreamEditing(val streamId: Long): NextDestination()
}