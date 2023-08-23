package me.lazy_assedninja.android_dumpling_timer.ui.timer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.DialogFragment
import me.lazy_assedninja.android_dumpling_timer.databinding.DialogConfirmRevertBinding
import me.lazy_assedninja.android_dumpling_timer.util.autoCleared

class ConfirmRevertDialog(val revertData: () -> Unit) : DialogFragment() {

    private var binding by autoCleared<DialogConfirmRevertBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DialogConfirmRevertBinding.inflate(inflater, container, false).apply {
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

        binding.btConfirm.setOnClickListener {
            revertData()
            dismiss()
        }
    }
}