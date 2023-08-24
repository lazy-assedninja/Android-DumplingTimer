package me.lazy_assedninja.android_dumpling_timer.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import me.lazy_assedninja.android_dumpling_timer.R
import me.lazy_assedninja.android_dumpling_timer.databinding.FragmentSettingBinding
import me.lazy_assedninja.android_dumpling_timer.ui.base.BaseFragment
import me.lazy_assedninja.android_dumpling_timer.util.autoCleared
import java.lang.Exception

class SettingFragment : BaseFragment() {

    private var binding by autoCleared<FragmentSettingBinding>()

    private val viewModel: SettingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btFinish.setOnClickListener {
                try {
                    if (etBaseTime.text.isEmpty() || etGapTime1.text.isEmpty() || etGapTime2.text.isEmpty() || etGapTime3.text.isEmpty() || etGapTime4.text.isEmpty() || etSoundEffectLoopTime.text.isEmpty()) {
                        Toast.makeText(it.context, R.string.toast_set_time_first, Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    viewModel.setSetting(
                        etBaseTime.text.toString().toLong(), listOf(
                            etGapTime1.text.toString().toLong(),
                            etGapTime2.text.toString().toLong(),
                            etGapTime3.text.toString().toLong(),
                            etGapTime4.text.toString().toLong()
                        ), etSoundEffectLoopTime.text.toString().toInt()
                    )
                    requireActivity().onBackPressed()
                } catch (e: NumberFormatException) {
                    Toast.makeText(it.context, R.string.toast_wrong_number_format, Toast.LENGTH_SHORT).show()
                }
            }

            lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.setting.collect {
                        if (it == null) return@collect
                        etBaseTime.setText(it.baseTime.toString())
                        etGapTime1.setText(it.gapTimeList[0].toString())
                        etGapTime2.setText(it.gapTimeList[1].toString())
                        etGapTime3.setText(it.gapTimeList[2].toString())
                        etGapTime4.setText(it.gapTimeList[3].toString())
                        etSoundEffectLoopTime.setText(it.soundEffectLoopTime.toString())
                    }
                }
            }
        }
    }
}
