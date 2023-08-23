package me.lazy_assedninja.android_dumpling_timer.ui.timer

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import me.lazy_assedninja.android_dumpling_timer.R
import me.lazy_assedninja.android_dumpling_timer.databinding.FragmentTimerBinding
import me.lazy_assedninja.android_dumpling_timer.ui.base.BaseFragment
import me.lazy_assedninja.android_dumpling_timer.util.SoundEffectUtils
import me.lazy_assedninja.android_dumpling_timer.util.autoCleared
import kotlin.math.roundToInt

class TimerFragment : BaseFragment() {

    companion object {
        const val INTERVAL = 1000L
    }

    private val viewModel: TimerViewModel by viewModels()

    private var binding by autoCleared<FragmentTimerBinding>()

    private val confirmRevertDialog: ConfirmRevertDialog by lazy {
        ConfirmRevertDialog {
            when (viewModel.revertData()) {
                -1 -> Toast.makeText(binding.root.context, R.string.toast_no_previous_step, Toast.LENGTH_SHORT).show()
                0 -> {
                    adapter1.submitList(viewModel.list1.toList())
                    if (viewModel.list1.isEmpty()) {
                        countDownTimer1?.cancel()
                        countDownTimer1 = null
                    }
                }

                1 -> {
                    adapter2.submitList(viewModel.list2.toList())
                    if (viewModel.list2.isEmpty()) {
                        countDownTimer2?.cancel()
                        countDownTimer2 = null
                    }
                }

                2 -> {
                    adapter3.submitList(viewModel.list3.toList())
                    if (viewModel.list3.isEmpty()) {
                        countDownTimer3?.cancel()
                        countDownTimer3 = null
                    }
                }

                3 -> {
                    adapter4.submitList(viewModel.list4.toList())
                    if (viewModel.list4.isEmpty()) {
                        countDownTimer4?.cancel()
                        countDownTimer4 = null
                    }
                }
            }
        }
    }
    private val doneDialog: DoneDialog by lazy {
        DoneDialog()
    }

    private val adapter1 by lazy { TimerRVAdapter((viewModel.setting?.baseTime ?: 0).toInt()) }
    private val adapter2 by lazy { TimerRVAdapter((viewModel.setting?.baseTime ?: 0).toInt()) }
    private val adapter3 by lazy { TimerRVAdapter((viewModel.setting?.baseTime ?: 0).toInt()) }
    private val adapter4 by lazy { TimerRVAdapter((viewModel.setting?.baseTime ?: 0).toInt()) }

    private val soundEffectUtils by lazy {
        SoundEffectUtils(binding.root.context)
    }

    private var countDownTimer1: CountDownTimer? = null
    private var countDownTimer2: CountDownTimer? = null
    private var countDownTimer3: CountDownTimer? = null
    private var countDownTimer4: CountDownTimer? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTimerBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel // For init.

        binding.apply {
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (!confirmRevertDialog.isVisible) confirmRevertDialog.show(parentFragmentManager, ConfirmRevertDialog::class.java.name)
                }
            })

            v1.setOnClickListener {
                if (viewModel.list1.size >= 5) {
                    Toast.makeText(it.context, R.string.toast_up_to_limit, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                viewModel.addData(0)
                adapter1.submitList(viewModel.list1.toList())

                if (viewModel.list1.size == 1) countDownTimer1 = createCountDownTimer1().start()
            }
            v2.setOnClickListener {
                if (viewModel.list2.size >= 5) {
                    Toast.makeText(it.context, R.string.toast_up_to_limit, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                viewModel.addData(1)
                adapter2.submitList(viewModel.list2.toList())

                if (viewModel.list2.size == 1) countDownTimer2 = createCountDownTimer2().start()
            }
            v3.setOnClickListener {
                if (viewModel.list3.size >= 5) {
                    Toast.makeText(it.context, R.string.toast_up_to_limit, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                viewModel.addData(2)
                adapter3.submitList(viewModel.list3.toList())

                if (viewModel.list3.size == 1) countDownTimer3 = createCountDownTimer3().start()
            }
            v4.setOnClickListener {
                if (viewModel.list4.size >= 5) {
                    Toast.makeText(it.context, R.string.toast_up_to_limit, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                viewModel.addData(3)
                adapter4.submitList(viewModel.list4.toList())

                if (viewModel.list4.size == 1) countDownTimer4 = createCountDownTimer4().start()
            }

            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.initSettingFinished.collect {
                        rv1.adapter = adapter1
                        rv2.adapter = adapter2
                        rv3.adapter = adapter3
                        rv4.adapter = adapter4
                    }
                }
            }
        }
    }

    private fun createCountDownTimer1(): CountDownTimer = object : CountDownTimer(viewModel.list1[0].percentage.roundToInt() * INTERVAL, INTERVAL) {
        @SuppressLint("NotifyDataSetChanged")
        override fun onTick(p0: Long) {
            viewModel.onTick(0)
            adapter1.notifyDataSetChanged()
        }

        override fun onFinish() {
            soundEffectUtils.playWarningSound(viewModel.setting?.soundEffectLoopTime ?: 0)

            val item = viewModel.list1.removeFirst()
            adapter1.submitList(viewModel.list1.toList())

            if (!doneDialog.isVisible) doneDialog.show(parentFragmentManager, DoneDialog::class.java.name)
            doneDialog.addData(item)

            countDownTimer1?.cancel()
            countDownTimer1 = null
            if (viewModel.list1.isNotEmpty()) countDownTimer1 = createCountDownTimer1().start()
        }
    }

    private fun createCountDownTimer2(): CountDownTimer = object : CountDownTimer(viewModel.list2[0].percentage.roundToInt() * INTERVAL, INTERVAL) {
        @SuppressLint("NotifyDataSetChanged")
        override fun onTick(p0: Long) {
            viewModel.onTick(1)
            adapter2.notifyDataSetChanged()
        }

        override fun onFinish() {
            soundEffectUtils.playWarningSound(viewModel.setting?.soundEffectLoopTime ?: 0)

            val item = viewModel.list2.removeFirst()
            adapter2.submitList(viewModel.list2.toList())

            if (!doneDialog.isVisible) doneDialog.show(parentFragmentManager, DoneDialog::class.java.name)
            doneDialog.addData(item)

            countDownTimer2?.cancel()
            countDownTimer2 = null
            if (viewModel.list2.isNotEmpty()) countDownTimer2 = createCountDownTimer2().start()
        }
    }

    private fun createCountDownTimer3(): CountDownTimer = object : CountDownTimer(viewModel.list3[0].percentage.roundToInt() * INTERVAL, INTERVAL) {
        @SuppressLint("NotifyDataSetChanged")
        override fun onTick(p0: Long) {
            viewModel.onTick(2)
            adapter3.notifyDataSetChanged()
        }

        override fun onFinish() {
            soundEffectUtils.playWarningSound(viewModel.setting?.soundEffectLoopTime ?: 0)

            val item = viewModel.list3.removeFirst()
            adapter3.submitList(viewModel.list3.toList())

            if (!doneDialog.isVisible) doneDialog.show(parentFragmentManager, DoneDialog::class.java.name)
            doneDialog.addData(item)

            countDownTimer3?.cancel()
            countDownTimer3 = null
            if (viewModel.list3.isNotEmpty()) countDownTimer3 = createCountDownTimer3().start()
        }
    }

    private fun createCountDownTimer4(): CountDownTimer = object : CountDownTimer(viewModel.list4[0].percentage.roundToInt() * INTERVAL, INTERVAL) {
        @SuppressLint("NotifyDataSetChanged")
        override fun onTick(p0: Long) {
            viewModel.onTick(3)
            adapter4.notifyDataSetChanged()
        }

        override fun onFinish() {
            soundEffectUtils.playWarningSound(viewModel.setting?.soundEffectLoopTime ?: 0)

            val item = viewModel.list4.removeFirst()
            adapter4.submitList(viewModel.list4.toList())

            if (!doneDialog.isVisible) doneDialog.show(parentFragmentManager, DoneDialog::class.java.name)
            doneDialog.addData(item)

            countDownTimer4?.cancel()
            countDownTimer4 = null
            if (viewModel.list4.isNotEmpty()) countDownTimer4 = createCountDownTimer4().start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        soundEffectUtils.release()
    }
}
