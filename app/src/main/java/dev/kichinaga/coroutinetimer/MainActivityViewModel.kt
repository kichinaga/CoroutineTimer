package dev.kichinaga.coroutinetimer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainActivityViewModel: ViewModel(), CoroutineScope {
    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val _state = MutableLiveData<TimerState>().also { it.value = TimerState.Stop }
    val state: LiveData<TimerState>
        get() = _state

    private val _timer = MutableLiveData<Long>().also { it.value = 0 }
    val timer: LiveData<Long>
        get() = _timer

    val timerText: LiveData<String> = Transformations.map(timer) { timer ->
        val hours = timer / 3600
        val minutes = (timer % 3600) / 60
        val seconds = (timer % 3600) % 60

        "%02d:%02d:%02d".format(hours, minutes, seconds)
    }

    fun changeTimerAction(changeState: TimerState) {
        when(state.value) {
            // カウントを止める
            is TimerState.Start -> {}
            // 前のカウントを引き継いでスタート
            is TimerState.Pause -> {
                _timer.value = timer.value
            }
            // カウントをリセットしてスタート
            is TimerState.Stop -> {
                _timer.value = 0L
            }
        }

        _state.value = changeState
    }

    fun countUpTimer() {
        launch {
            delay(1000L)
            if (state.value == TimerState.Start) {
                _timer.value = timer.value?.plus(1)
            }
        }
    }

    override fun onCleared() {
        job.cancelChildren()
        super.onCleared()
    }
}