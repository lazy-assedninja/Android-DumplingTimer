package me.lazy_assedninja.android_dumpling_timer.ui.option

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import me.lazy_assedninja.android_dumpling_timer.R
import me.lazy_assedninja.android_dumpling_timer.databinding.FragmentOptionBinding
import me.lazy_assedninja.android_dumpling_timer.ui.base.BaseFragment
import me.lazy_assedninja.android_dumpling_timer.ui.setting.SettingViewModel
import me.lazy_assedninja.android_dumpling_timer.util.autoCleared

class OptionFragment : BaseFragment() {

    private val settingViewModel: SettingViewModel by viewModels()

    private var binding by autoCleared<FragmentOptionBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentOptionBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btSetting.setOnClickListener {
                findNavController().navigate(OptionFragmentDirections.toSettingFragment())
            }

            lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    settingViewModel.setting.collect { setting ->
                        btTimer.setOnClickListener {
                            if (setting == null) Snackbar.make(root, R.string.toast_set_time_first, Snackbar.LENGTH_SHORT).show()
                            else findNavController().navigate(OptionFragmentDirections.toTimerFragment())
                        }
                    }
                }
            }
        }
    }
}
