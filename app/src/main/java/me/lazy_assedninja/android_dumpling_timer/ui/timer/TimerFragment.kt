package me.lazy_assedninja.android_dumpling_timer.ui.timer

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
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
import me.lazy_assedninja.android_dumpling_timer.data.vo.Time
import me.lazy_assedninja.android_dumpling_timer.databinding.FragmentTimerBinding
import me.lazy_assedninja.android_dumpling_timer.ui.base.BaseFragment
import me.lazy_assedninja.android_dumpling_timer.ui.confirm_revert.ConfirmRevertViewModel
import me.lazy_assedninja.android_dumpling_timer.ui.done.DoneViewModel
import me.lazy_assedninja.android_dumpling_timer.util.autoCleared
import timber.log.Timber
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

    @SuppressLint("NotifyDataSetChanged")
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
            }
            v2.setOnClickListener {
                if (!viewModel.addData(1)) showReachLimitSnackbar()
            }
            v3.setOnClickListener {
                if (!viewModel.addData(2)) showReachLimitSnackbar()
            }
            v4.setOnClickListener {
                if (!viewModel.addData(3)) showReachLimitSnackbar()
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
                        viewModel.revertData()
                    }
                }
            }

            lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.list1UiState.collect { uiState ->
                        when (uiState) {
                            is ListUiState.TimeList -> {
                                Timber.tag("yyy").d("list1UiState collect")
                                viewModel.cacheList1.clear()
                                viewModel.cacheList1.addAll(uiState.list)

//                                if (adapter1.itemCount == uiState.list.size) adapter1.notifyDataSetChanged()
//                                else
                                Timber.tag("xxxxx").d("cached list: ${viewModel.cacheList1}")
                                adapter1.submitList(viewModel.cacheList1.toList())

                                if (countDownTimer1 == null && uiState.list.isNotEmpty()) countDownTimer1 =
                                    createCountDownTimer1(uiState.list[0]).start()
                            }

                            else -> {}
                        }
                    }
                }
            }
            lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.list2UiState.collect { uiState ->
                        when (uiState) {
                            is ListUiState.TimeList -> {
                                viewModel.cacheList2.clear()
                                viewModel.cacheList2.addAll(uiState.list)

                                if (adapter2.itemCount == uiState.list.size) adapter2.notifyDataSetChanged()
                                else adapter2.submitList(viewModel.cacheList2)

                                if (countDownTimer2 == null && uiState.list.isNotEmpty()) countDownTimer2 =
                                    createCountDownTimer2(uiState.list[1]).start()
                            }

                            else -> {}
                        }
                    }
                }
            }
            lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.list3UiState.collect { uiState ->
                        when (uiState) {
                            is ListUiState.TimeList -> {
                                viewModel.cacheList3.clear()
                                viewModel.cacheList3.addAll(uiState.list)

                                if (adapter3.itemCount == uiState.list.size) adapter3.notifyDataSetChanged()
                                else adapter3.submitList(viewModel.cacheList3)

                                if (countDownTimer3 == null && uiState.list.isNotEmpty()) countDownTimer3 =
                                    createCountDownTimer3(uiState.list[2]).start()
                            }

                            else -> {}
                        }
                    }
                }
            }
            lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.list4UiState.collect { uiState ->
                        when (uiState) {
                            is ListUiState.TimeList -> {
                                viewModel.cacheList4.clear()
                                viewModel.cacheList4.addAll(uiState.list)

                                if (adapter4.itemCount == uiState.list.size) adapter4.notifyDataSetChanged()
                                else adapter4.submitList(viewModel.cacheList4)

                                if (countDownTimer4 == null && uiState.list.isNotEmpty()) countDownTimer4 =
                                    createCountDownTimer4(uiState.list[3]).start()
                            }

                            else -> {}
                        }
                    }
                }
            }
        }
    }

    private fun createCountDownTimer1(time: Time): CountDownTimer = object : CountDownTimer(time.percentage.roundToInt() * INTERVAL, INTERVAL) {
        @SuppressLint("NotifyDataSetChanged")
        override fun onTick(p0: Long) {
            viewModel.onTick(0)
        }

        override fun onFinish() {
            countDownTimer1?.cancel()
            countDownTimer1 = null

            val item = viewModel.doneData(0)

            if (findNavController().currentDestination?.id == R.id.timer_fragment) findNavController().navigate(TimerFragmentDirections.showDoneDialog())
            doneViewModel.addData(item)
        }
    }

    private fun createCountDownTimer2(time: Time): CountDownTimer =
        object : CountDownTimer((time.percentage * INTERVAL).roundToInt().toLong(), INTERVAL) {
            @SuppressLint("NotifyDataSetChanged")
            override fun onTick(p0: Long) {
                viewModel.onTick(1)
            }

            override fun onFinish() {
                val item = viewModel.doneData(1)

                if (findNavController().currentDestination?.id == R.id.timer_fragment) findNavController().navigate(TimerFragmentDirections.showDoneDialog())
                doneViewModel.addData(item)

                countDownTimer2?.cancel()
                countDownTimer2 = null
            }
        }

    private fun createCountDownTimer3(time: Time): CountDownTimer =
        object : CountDownTimer((time.percentage * INTERVAL).roundToInt().toLong(), INTERVAL) {
            @SuppressLint("NotifyDataSetChanged")
            override fun onTick(p0: Long) {
                viewModel.onTick(2)
            }

            override fun onFinish() {
                val item = viewModel.doneData(2)

                if (findNavController().currentDestination?.id == R.id.timer_fragment) findNavController().navigate(TimerFragmentDirections.showDoneDialog())
                doneViewModel.addData(item)

                countDownTimer3?.cancel()
                countDownTimer3 = null
            }
        }

    private fun createCountDownTimer4(time: Time): CountDownTimer =
        object : CountDownTimer((time.percentage * INTERVAL).roundToInt().toLong(), INTERVAL) {
            @SuppressLint("NotifyDataSetChanged")
            override fun onTick(p0: Long) {
                viewModel.onTick(3)
            }

            override fun onFinish() {
                val item = viewModel.doneData(3)

                if (findNavController().currentDestination?.id == R.id.timer_fragment) findNavController().navigate(TimerFragmentDirections.showDoneDialog())
                doneViewModel.addData(item)

                countDownTimer4?.cancel()
                countDownTimer4 = null
            }
        }
}
