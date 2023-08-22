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
import androidx.recyclerview.widget.LinearLayoutManager
import me.lazy_assedninja.android_dumpling_timer.R
import me.lazy_assedninja.android_dumpling_timer.databinding.FragmentTimerBinding
import me.lazy_assedninja.android_dumpling_timer.ui.base.BaseFragment
import me.lazy_assedninja.android_dumpling_timer.util.SoundEffectUtils
import me.lazy_assedninja.android_dumpling_timer.util.autoCleared
import timber.log.Timber

class TimerFragment : BaseFragment() {

    companion object {
        const val INTERVAL = 1000L
    }

    private val viewModel: TimerViewModel by viewModels()

    private var binding by autoCleared<FragmentTimerBinding>()

    private val dialog: DoneDialog by lazy {
        DoneDialog()
    }

    private val adapter1 = TimerRVAdapter(Type.Timer)
    private val adapter2 = TimerRVAdapter(Type.Timer)
    private val adapter3 = TimerRVAdapter(Type.Timer)
    private val adapter4 = TimerRVAdapter(Type.Timer)

    private var countDownTimer1: CountDownTimer? = null
    private var countDownTimer2: CountDownTimer? = null
    private var countDownTimer3: CountDownTimer? = null
    private var countDownTimer4: CountDownTimer? = null

    private var soundEffectUtils: SoundEffectUtils? = null

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
                    when (viewModel.revertData()) {
                        0 -> adapter1.submitList(viewModel.list1.toList())
                        1 -> adapter2.submitList(viewModel.list2.toList())
                        2 -> adapter3.submitList(viewModel.list3.toList())
                        3 -> adapter4.submitList(viewModel.list4.toList())
                        -1 -> Toast.makeText(root.context, R.string.toast_no_previous_step, Toast.LENGTH_SHORT).show()
                    }
                }
            })

            (rv1.layoutManager as LinearLayoutManager).reverseLayout = true
            (rv2.layoutManager as LinearLayoutManager).reverseLayout = true
            (rv3.layoutManager as LinearLayoutManager).reverseLayout = true
            (rv4.layoutManager as LinearLayoutManager).reverseLayout = true

            rv1.adapter = adapter1
            rv2.adapter = adapter2
            rv3.adapter = adapter3
            rv4.adapter = adapter4

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

            soundEffectUtils = SoundEffectUtils(root.context)
        }
    }

    private fun createCountDownTimer1(): CountDownTimer = object : CountDownTimer(viewModel.list1[0].time * INTERVAL, INTERVAL) {
        @SuppressLint("NotifyDataSetChanged")
        override fun onTick(p0: Long) {
            for (time in viewModel.list1) {
                time.time -= 1
                Timber.tag("xxxxx").d("time: %s", time.time)
            }
            Timber.tag("xxxxx").d("list1: %s", viewModel.list1)
            adapter1.notifyDataSetChanged()
        }

        override fun onFinish() {
            val item = viewModel.list1.removeFirst()
            adapter1.submitList(viewModel.list1.toList())
            countDownTimer1?.cancel()

            if (!dialog.isVisible) dialog.show(parentFragmentManager, DoneDialog::class.java.name)
            dialog.addData(item)

            soundEffectUtils?.playWarningSound()

            if (viewModel.list1.isEmpty()) return

            countDownTimer1 = createCountDownTimer1().start()
        }
    }

    private fun createCountDownTimer2(): CountDownTimer = object : CountDownTimer(viewModel.list2[0].time * INTERVAL, INTERVAL) {
        @SuppressLint("NotifyDataSetChanged")
        override fun onTick(p0: Long) {
            for (time in viewModel.list2) {
                time.time -= 1
                Timber.tag("xxxxx").d("time: %s", time.time)
            }
            Timber.tag("xxxxx").d("list2: %s", viewModel.list2)
            adapter2.notifyDataSetChanged()
        }

        override fun onFinish() {
            val item = viewModel.list2.removeFirst()
            adapter2.submitList(viewModel.list2.toList())
            countDownTimer2?.cancel()

            if (!dialog.isVisible) dialog.show(parentFragmentManager, DoneDialog::class.java.name)
            dialog.addData(item)

            soundEffectUtils?.playWarningSound()

            if (viewModel.list2.isEmpty()) return

            countDownTimer2 = createCountDownTimer2().start()
        }
    }

    private fun createCountDownTimer3(): CountDownTimer = object : CountDownTimer(viewModel.list3[0].time * INTERVAL, INTERVAL) {
        @SuppressLint("NotifyDataSetChanged")
        override fun onTick(p0: Long) {
            for (time in viewModel.list3) {
                time.time -= 1
                Timber.tag("xxxxx").d("time: %s", time.time)
            }
            Timber.tag("xxxxx").d("list3: %s", viewModel.list3)
            adapter3.notifyDataSetChanged()
        }

        override fun onFinish() {
            val item = viewModel.list3.removeFirst()
            adapter3.submitList(viewModel.list3.toList())
            countDownTimer3?.cancel()

            if (!dialog.isVisible) dialog.show(parentFragmentManager, DoneDialog::class.java.name)
            dialog.addData(item)

            soundEffectUtils?.playWarningSound()

            if (viewModel.list3.isEmpty()) return

            countDownTimer3 = createCountDownTimer3().start()
        }
    }

    private fun createCountDownTimer4(): CountDownTimer = object : CountDownTimer(viewModel.list4[0].time * INTERVAL, INTERVAL) {
        @SuppressLint("NotifyDataSetChanged")
        override fun onTick(p0: Long) {
            for (time in viewModel.list4) {
                time.time -= 1
                Timber.tag("xxxxx").d("time: %s", time.time)
            }
            Timber.tag("xxxxx").d("list4: %s", viewModel.list4)
            adapter4.notifyDataSetChanged()
        }

        override fun onFinish() {
            val item = viewModel.list4.removeFirst()
            adapter4.submitList(viewModel.list4.toList())
            countDownTimer4?.cancel()

            if (!dialog.isVisible) dialog.show(parentFragmentManager, DoneDialog::class.java.name)
            dialog.addData(item)

            soundEffectUtils?.playWarningSound()

            if (viewModel.list4.isEmpty()) return

            countDownTimer4 = createCountDownTimer4().start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        soundEffectUtils?.release()
    }
}
