package tv.wfc.livestreamsales.features.liveBroadcastingSettingsDestination.model.navigation

sealed class NextDestination{
    object Close: NextDestination()
    data class LiveBroadcasting(
        val serverAddress: String,
        val serverPort: Int,
        val streamKey: String,
        val sourceUserName: String,
        val sourcePassword: String
    ): NextDestination()
}