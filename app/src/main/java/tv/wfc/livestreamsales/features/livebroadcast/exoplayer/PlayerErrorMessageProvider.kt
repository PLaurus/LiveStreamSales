package tv.wfc.livestreamsales.features.livebroadcast.exoplayer

import android.content.Context
import android.content.res.Resources
import android.util.Pair
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil
import com.google.android.exoplayer2.util.ErrorMessageProvider
import tv.wfc.livestreamsales.R
import javax.inject.Inject

class PlayerErrorMessageProvider @Inject constructor(
    private val context: Context
): ErrorMessageProvider<PlaybackException> {
    private val resources: Resources
        get() = context.resources

    override fun getErrorMessage(playbackException: PlaybackException): Pair<Int, String> {
        var errorMessage = resources.getString(R.string.player_error_generic)
        val cause = playbackException.cause

        if (cause is MediaCodecRenderer.DecoderInitializationException) {
            // Special case for decoder initialization failures.

            errorMessage = if (cause.codecInfo == null) {
                when {
                    cause is MediaCodecUtil.DecoderQueryException -> {
                        resources.getString(R.string.player_error_querying_decoders)
                    }
                    cause.secureDecoderRequired -> {
                        resources.getString(
                            R.string.player_error_no_secure_decoder,
                            cause.mimeType
                        )
                    }
                    else -> {
                        resources.getString(
                            R.string.player_error_no_decoder,
                            cause.mimeType
                        )
                    }
                }
            } else {
                resources.getString(
                    R.string.player_error_instantiating_decoder,
                    cause.codecInfo?.name
                )
            }
        }

        return Pair(0, errorMessage)
    }
}