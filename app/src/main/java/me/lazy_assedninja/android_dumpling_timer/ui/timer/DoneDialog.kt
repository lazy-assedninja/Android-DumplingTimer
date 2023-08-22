package me.lazy_assedninja.android_dumpling_timer.ui.timer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.DialogFragment
import me.lazy_assedninja.android_dumpling_timer.data.vo.Time
import me.lazy_assedninja.android_dumpling_timer.databinding.DialogDoneBinding
import me.lazy_assedninja.android_dumpling_timer.util.autoCleared
import timber.log.Timber

class DoneDialog : DialogFragment() {

    private var binding by autoCleared<DialogDoneBinding>()

    private val adapter = TimerRVAdapter(Type.Done) {
        Timber.tag("yyy").d("dismiss")
        dismiss()
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
    }

    fun addData(item: Time) {
        adapter.submitList(adapter.currentList.toMutableList().apply {
            add(item)
            Timber.tag("yyy").d("$this")
        })
    }
}
