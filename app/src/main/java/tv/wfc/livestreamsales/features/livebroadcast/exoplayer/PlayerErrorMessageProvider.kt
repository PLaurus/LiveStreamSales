package tv.wfc.livestreamsales.features.livebroadcast.exoplayer

import android.content.Context
import android.content.res.Resources
import android.util.Pair
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil
import com.google.android.exoplayer2.util.ErrorMessageProvider
import tv.wfc.livestreamsales.R
import javax.inject.Inject

class PlayerErrorMessageProvider @Inject constructor(
    private val context: Context
): ErrorMessageProvider<ExoPlaybackException> {
    private val resources: Resources
        get() = context.resources

    override fun getErrorMessage(playbackException: ExoPlaybackException): Pair<Int, String> {
        var errorMessage = resources.getString(R.string.player_error_generic)

        if (playbackException.type == ExoPlaybackException.TYPE_RENDERER) {
            val rendererException = playbackException.rendererException

            if (rendererException is MediaCodecRenderer.DecoderInitializationException) {
                // Special case for decoder initialization failures.
                errorMessage = if (rendererException.codecInfo == null) {
                    when {
                        rendererException.cause is MediaCodecUtil.DecoderQueryException -> {
                            resources.getString(R.string.player_error_querying_decoders)
                        }
                        rendererException.secureDecoderRequired -> {
                            resources.getString(
                                R.string.player_error_no_secure_decoder,
                                rendererException.mimeType
                            )
                        }
                        else -> {
                            resources.getString(
                                R.string.player_error_no_decoder,
                                rendererException.mimeType
                            )
                        }
                    }
                } else {
                    resources.getString(
                        R.string.player_error_instantiating_decoder,
                        rendererException.codecInfo?.name
                    )
                }
            }
        }

        return Pair(0, errorMessage)
    }
}