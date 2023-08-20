package me.lazy_assedninja.android_dumpling_timer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import me.lazy_assedninja.android_dumpling_timer.databinding.FragmentOptionBinding
import me.lazy_assedninja.android_dumpling_timer.ui.base.BaseFragment
import me.lazy_assedninja.android_dumpling_timer.util.autoCleared

class OptionFragment : BaseFragment() {

    private var binding by autoCleared<FragmentOptionBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentOptionBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btTimer.setOnClickListener {
            findNavController().navigate(OptionFragmentDirections.toTimerFragment())
        }
        binding.btSetting.setOnClickListener {
            findNavController().navigate(OptionFragmentDirections.toSettingFragment())
        }
    }
}