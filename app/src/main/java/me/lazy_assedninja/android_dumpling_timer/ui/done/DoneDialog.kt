package me.lazy_assedninja.android_dumpling_timer.ui.done

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import me.lazy_assedninja.android_dumpling_timer.data.vo.Time
import me.lazy_assedninja.android_dumpling_timer.databinding.DialogDoneBinding
import me.lazy_assedninja.android_dumpling_timer.ui.timer.TimerViewModel
import me.lazy_assedninja.android_dumpling_timer.util.SoundEffectUtils
import me.lazy_assedninja.android_dumpling_timer.util.autoCleared

class DoneDialog : DialogFragment() {

    private val viewModel: DoneViewModel by viewModels()

    private var binding by autoCleared<DialogDoneBinding>()

    private val adapter = DoneRVAdapter { dismiss() }

    private val soundEffectUtils by lazy {
        SoundEffectUtils(binding.root.context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DialogDoneBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Do nothing
            }
        })

        binding.apply {
            rv.adapter = adapter
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.setting.collect {
                    soundEffectUtils.playWarningSound(it?.soundEffectLoopTime ?: 0)
                }
            }
        }
    }

    fun addData(item: Time) {
        adapter.submitList(adapter.currentList.toMutableList().apply {
            add(item)
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        soundEffectUtils.release()
    }
}
