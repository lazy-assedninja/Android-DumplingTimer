package me.lazy_assedninja.android_dumpling_timer.ui.timer

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import me.lazy_assedninja.android_dumpling_timer.R
import me.lazy_assedninja.android_dumpling_timer.databinding.FragmentTimerBinding
import me.lazy_assedninja.android_dumpling_timer.ui.base.BaseFragment
import me.lazy_assedninja.android_dumpling_timer.ui.confirm_revert.ConfirmRevertViewModel
import me.lazy_assedninja.android_dumpling_timer.ui.done.DoneViewModel
import me.lazy_assedninja.android_dumpling_timer.util.autoCleared
import kotlin.math.roundToInt

class TimerFragment : BaseFragment() {

    companion object {
        const val INTERVAL = 1000L
    }

    private val viewModel: TimerViewModel by viewModels()
    private val doneViewModel: DoneViewModel by activityViewModels()
    private val confirmRevertViewModel: ConfirmRevertViewModel by activityViewModels()

    private var binding by autoCleared<FragmentTimerBinding>()

    private val adapter1 by lazy { TimerRVAdapter((viewModel.setting?.baseTime ?: 0).toInt()) }
    private val adapter2 by lazy { TimerRVAdapter((viewModel.setting?.baseTime ?: 0).toInt()) }
    private val adapter3 by lazy { TimerRVAdapter((viewModel.setting?.baseTime ?: 0).toInt()) }
    private val adapter4 by lazy { TimerRVAdapter((viewModel.setting?.baseTime ?: 0).toInt()) }

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
        binding.apply {
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (viewModel.currentTimerList.isEmpty()) Snackbar.make(binding.root, R.string.toast_no_previous_step, Snackbar.LENGTH_SHORT)
                        .show()
                    else if (findNavController().currentDestination?.id == R.id.timer_fragment) findNavController().navigate(TimerFragmentDirections.showConfirmRevertDialog())
                }
            })

            fun showReachLimitSnackbar() {
                Snackbar.make(binding.root, R.string.toast_up_to_limit, Snackbar.LENGTH_SHORT).show()
            }

            v1.setOnClickListener {
                if (!viewModel.addData(0)) showReachLimitSnackbar()

                if (viewModel.list1.size == 1) countDownTimer1 = createCountDownTimer1().start()
            }
            v2.setOnClickListener {
                if (!viewModel.addData(0)) showReachLimitSnackbar()

                if (viewModel.list2.size == 1) countDownTimer2 = createCountDownTimer2().start()
            }
            v3.setOnClickListener {
                if (!viewModel.addData(0)) showReachLimitSnackbar()

                if (viewModel.list3.size == 1) countDownTimer3 = createCountDownTimer3().start()
            }
            v4.setOnClickListener {
                if (!viewModel.addData(0)) showReachLimitSnackbar()

                if (viewModel.list4.size == 1) countDownTimer4 = createCountDownTimer4().start()
            }

            lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.initSettingFinished.collect {
                        rv1.adapter = adapter1
                        rv2.adapter = adapter2
                        rv3.adapter = adapter3
                        rv4.adapter = adapter4
                    }
                }
            }

            lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    confirmRevertViewModel.confirmClick.collect {
                        when (viewModel.revertData()) {
                            0 -> if (viewModel.list1.isEmpty()) {
                                countDownTimer1?.cancel()
                                countDownTimer1 = null
                            }

                            1 -> if (viewModel.list2.isEmpty()) {
                                countDownTimer2?.cancel()
                                countDownTimer2 = null
                            }

                            2 -> if (viewModel.list3.isEmpty()) {
                                countDownTimer3?.cancel()
                                countDownTimer3 = null
                            }

                            3 -> if (viewModel.list4.isEmpty()) {
                                countDownTimer4?.cancel()
                                countDownTimer4 = null
                            }
                        }
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
            val item = viewModel.doneData(0)

            if (findNavController().currentDestination?.id == R.id.timer_fragment) findNavController().navigate(TimerFragmentDirections.showDoneDialog())
            doneViewModel.addData(item)

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
            val item = viewModel.doneData(1)

            if (findNavController().currentDestination?.id == R.id.timer_fragment) findNavController().navigate(TimerFragmentDirections.showDoneDialog())
            doneViewModel.addData(item)

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
            val item = viewModel.doneData(2)

            if (findNavController().currentDestination?.id == R.id.timer_fragment) findNavController().navigate(TimerFragmentDirections.showDoneDialog())
            doneViewModel.addData(item)

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
            val item = viewModel.doneData(3)

            if (findNavController().currentDestination?.id == R.id.timer_fragment) findNavController().navigate(TimerFragmentDirections.showDoneDialog())
            doneViewModel.addData(item)

            countDownTimer4?.cancel()
            countDownTimer4 = null
            if (viewModel.list4.isNotEmpty()) countDownTimer4 = createCountDownTimer4().start()
        }
    }
}
