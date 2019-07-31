package dev.kichinaga.coroutinetimer

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

sealed class TimerState(@DrawableRes val icon: Int, @ColorRes val color: Int) {
    object Start: TimerState(R.drawable.ic_stop_white_24dp, R.color.colorError)
    object Pause: TimerState(R.drawable.ic_pause_white_24dp, R.color.colorError)
    object Stop: TimerState(R.drawable.ic_play_arrow_white_24dp, R.color.colorAccent)
}