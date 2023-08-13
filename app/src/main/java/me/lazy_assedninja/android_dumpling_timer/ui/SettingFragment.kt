package me.lazy_assedninja.android_dumpling_timer.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import me.lazy_assedninja.android_dumpling_timer.databinding.FragmentSettingBinding
import me.lazy_assedninja.android_dumpling_timer.ui.base.BaseFragment
import me.lazy_assedninja.android_dumpling_timer.util.autoCleared
import timber.log.Timber

class SettingFragment : BaseFragment() {

    private var binding by autoCleared<FragmentSettingBinding>()

    private val viewModel: SettingViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSettingBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btFinish.setOnClickListener {
                viewModel.setSetting(etBaseTime.text.toString().toInt(), etGapTime.text.toString().toInt(), etSoundEffectTime.text.toString().toInt())
                requireActivity().onBackPressed()
            }

            lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.setting.collect {
                        etBaseTime.setText(it.baseTime.toString())
                        etGapTime.setText(it.gapTime.toString())
                        etSoundEffectTime.setText(it.soundEffectTime.toString())
                    }
                }
            }
        }
    }
}