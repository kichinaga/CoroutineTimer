package dev.kichinaga.coroutinetimer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import dev.kichinaga.coroutinetimer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        val viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        binding.viewModel = viewModel

        binding.timerAction.setOnClickListener {
            when (viewModel.state.value) {
                TimerState.Start -> viewModel.changeTimerAction(TimerState.Stop)
                TimerState.Pause,
                TimerState.Stop -> viewModel.changeTimerAction(TimerState.Start)
            }
        }
        binding.timerAction.setOnLongClickListener {
            if (viewModel.state.value == TimerState.Start) {
                viewModel.changeTimerAction(TimerState.Pause)
                return@setOnLongClickListener true
            }
            return@setOnLongClickListener false
        }

        viewModel.timer.observe(this, Observer {
            viewModel.countUpTimer()
        })

        viewModel.state.observe(this, Observer { timerState ->
            binding.timerAction.setImageResource(timerState.icon)
            binding.timerAction.backgroundTintList = getColorStateList(timerState.color)
        })
    }
}
