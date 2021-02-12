package tv.wfc.livestreamsales.application.tools.exoplayer

import com.google.android.exoplayer2.Player

enum class PlaybackState{
    STATE_IDLE,
    STATE_BUFFERING,
    STATE_READY,
    STATE_ENDED;

    companion object{
        fun fromInt(@Player.State playbackStateInt: Int): PlaybackState {
            return when(playbackStateInt){
                Player.STATE_IDLE -> STATE_IDLE
                Player.STATE_BUFFERING -> STATE_BUFFERING
                Player.STATE_READY -> STATE_READY
                Player.STATE_ENDED -> STATE_ENDED
                else -> STATE_IDLE
            }
        }
    }
}