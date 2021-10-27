package tv.wfc.livestreamsales.application.model.streamingservice

data class StreamingService(
    /**
     * Streaming service server address.
     */
    val serverAddress: String,
    /**
     * Stream name. In some cases may be interpreted as stream key.
     */
    val streamName: String,
    /**
     * Streaming service server port used for connection. It can be null in some cases to indicate
     * that default port must be used.
     */
    val serverPort: Int? = null,
    /**
     * Some streaming services offer authentication feature. This field is null when there is no
     * need in authentication. And is not null when connection authorization must be used.
     */
    val authenticationData: AuthenticationData? = null
) {
    data class AuthenticationData(
        /**
         * Login that must be used during connection authorization.
         */
        val userName: String,
        /**
         * Password that must be used during connection authorization.
         */
        val password: String
    )
}