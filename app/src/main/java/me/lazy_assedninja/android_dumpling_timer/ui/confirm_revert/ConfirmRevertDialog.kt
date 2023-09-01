package me.lazy_assedninja.android_dumpling_timer.ui.confirm_revert

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
import me.lazy_assedninja.android_dumpling_timer.databinding.DialogConfirmRevertBinding
import me.lazy_assedninja.android_dumpling_timer.util.autoCleared

class ConfirmRevertDialog : DialogFragment() {

    private val viewModel: ConfirmRevertViewModel by activityViewModels()

    private var binding by autoCleared<DialogConfirmRevertBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)

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
            viewModel.confirmClick()
            dismiss()
        }
    }
}
