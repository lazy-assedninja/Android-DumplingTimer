package me.lazy_assedninja.android_dumpling_timer.ui.done

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import me.lazy_assedninja.android_dumpling_timer.databinding.DialogDoneBinding
import me.lazy_assedninja.android_dumpling_timer.util.SoundEffectUtils
import me.lazy_assedninja.android_dumpling_timer.util.autoCleared

class DoneDialog : DialogFragment() {

    private val viewModel: DoneViewModel by activityViewModels()

    private var binding by autoCleared<DialogDoneBinding>()

    private val adapter = DoneRVAdapter { item -> viewModel.removeData(item) }

    private lateinit var soundEffectUtils: SoundEffectUtils

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)

        binding = DialogDoneBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false

        soundEffectUtils = SoundEffectUtils(binding.root.context)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Do nothing
            }
        })

        binding.rv.adapter = adapter

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.setting.collect {
                    soundEffectUtils.playWarningSound(it?.soundEffectLoopTime ?: 0)
                }
            }
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    when (uiState) {
                        is UiState.DoneList -> {
                            adapter.submitList(uiState.list.toList())
                            if (uiState.list.isEmpty()) dismiss()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        soundEffectUtils.release()
    }
}
