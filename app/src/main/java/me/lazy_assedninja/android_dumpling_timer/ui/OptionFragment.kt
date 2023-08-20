package me.lazy_assedninja.android_dumpling_timer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import me.lazy_assedninja.android_dumpling_timer.R
import me.lazy_assedninja.android_dumpling_timer.databinding.FragmentOptionBinding
import me.lazy_assedninja.android_dumpling_timer.ui.base.BaseFragment
import me.lazy_assedninja.android_dumpling_timer.util.autoCleared

class OptionFragment : BaseFragment() {

    private var binding by autoCleared<FragmentOptionBinding>()

    private val timerViewModel: TimerViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentOptionBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        timerViewModel // For init.

        binding.btTimer.setOnClickListener {
            if (timerViewModel.setting == null) Toast.makeText(it.context, R.string.toast_set_time_first, Toast.LENGTH_SHORT).show()
            else findNavController().navigate(OptionFragmentDirections.toTimerFragment())
        }
        binding.btSetting.setOnClickListener {
            findNavController().navigate(OptionFragmentDirections.toSettingFragment())
        }
    }
}