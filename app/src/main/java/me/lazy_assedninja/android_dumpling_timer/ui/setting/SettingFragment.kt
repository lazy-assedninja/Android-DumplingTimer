package me.lazy_assedninja.android_dumpling_timer.ui.setting

import android.os.Bundle
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
                viewModel.setSetting(etBaseTime.text.toString().toLong(), etGapTime.text.toString().toLong(), etSoundEffectTime.text.toString().toLong())
                requireActivity().onBackPressed()
            }

            lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.setting.collect {
                        if (it == null) return@collect
                        etBaseTime.setText(it.baseTime.toString())
                        etGapTime.setText(it.gapTime.toString())
                        etSoundEffectTime.setText(it.soundEffectTime.toString())
                    }
                }
            }
        }
    }
}
