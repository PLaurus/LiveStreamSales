package tv.wfc.livestreamsales.features.mystreams.model

sealed class NextDestination {
    object Close: NextDestination()
    object StreamCreation: NextDestination()
}