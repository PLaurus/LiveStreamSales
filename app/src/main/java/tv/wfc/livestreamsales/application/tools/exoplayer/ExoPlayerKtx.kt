package tv.wfc.livestreamsales.application.tools.exoplayer

import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.source.BehindLiveWindowException

fun ExoPlaybackException.isBehindLiveWindow(): Boolean{
    if (type != ExoPlaybackException.TYPE_SOURCE) {
        return false
    }

    var cause: Throwable? = sourceException

    while (cause != null) {
        if (cause is BehindLiveWindowException) {
            return true
        }

        cause = cause.cause
    }

    return false
}