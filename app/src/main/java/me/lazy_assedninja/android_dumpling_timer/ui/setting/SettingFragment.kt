package me.lazy_assedninja.android_dumpling_timer.ui.setting

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch
import me.lazy_assedninja.android_dumpling_timer.R
import me.lazy_assedninja.android_dumpling_timer.databinding.FragmentSettingBinding
import me.lazy_assedninja.android_dumpling_timer.ui.base.BaseFragment
import me.lazy_assedninja.android_dumpling_timer.util.autoCleared

class SettingFragment : BaseFragment() {

    private var binding by autoCleared<FragmentSettingBinding>()

    private val viewModel: SettingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            fun Editable?.checkNumberFormat(textInputLayout: TextInputLayout) {
                try {
                    this.toString().toLong()
                    textInputLayout.error = null
                } catch (e: NumberFormatException) {
                    textInputLayout.error = getString(R.string.error_wrong_time_format)
                }
            }
            tieBaseTime.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    s.checkNumberFormat(tilBaseTime)
                }
            })
            tieGapTime1.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    s.checkNumberFormat(tilGapTime1)
                }
            })
            tieGapTime2.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    s.checkNumberFormat(tilGapTime2)
                }
            })
            tieGapTime3.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    s.checkNumberFormat(tilGapTime3)
                }
            })
            tieGapTime4.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    s.checkNumberFormat(tilGapTime4)
                }
            })
            tieSoundEffectLoopTime.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    try {
                        s.toString().toInt()
                        tilSoundEffectLoopTime.error = null
                    } catch (e: NumberFormatException) {
                        tilSoundEffectLoopTime.error = getString(R.string.error_wrong_number_format)
                    }
                }
            })

            btFinish.setOnClickListener {
                dismissKeyboard(it.windowToken)
                if (tieBaseTime.text.isNullOrEmpty() || tieGapTime1.text.isNullOrEmpty() || tieGapTime2.text.isNullOrEmpty() || tieGapTime3.text.isNullOrEmpty() || tieGapTime4.text.isNullOrEmpty() || tieSoundEffectLoopTime.text.isNullOrEmpty()) {
                    Snackbar.make(binding.root, R.string.toast_set_time_first, Snackbar.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (!tilBaseTime.error.isNullOrEmpty() || !tilGapTime1.error.isNullOrEmpty() || !tilGapTime2.error.isNullOrEmpty() || !tilGapTime3.error.isNullOrEmpty() || !tilGapTime4.error.isNullOrEmpty() || !tilSoundEffectLoopTime.error.isNullOrEmpty()) {
                    Snackbar.make(binding.root, R.string.error_wrong_time_format, Snackbar.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                viewModel.setSetting(
                    tieBaseTime.text.toString().toLong(), listOf(
                        tieGapTime1.text.toString().toLong(),
                        tieGapTime2.text.toString().toLong(),
                        tieGapTime3.text.toString().toLong(),
                        tieGapTime4.text.toString().toLong()
                    ), tieSoundEffectLoopTime.text.toString().toInt()
                )
                requireActivity().onBackPressed()
            }

            lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.setting.collect {
                        if (it == null) return@collect
                        tieBaseTime.setText(it.baseTime.toString())
                        tieGapTime1.setText(it.gapTimeList[0].toString())
                        tieGapTime2.setText(it.gapTimeList[1].toString())
                        tieGapTime3.setText(it.gapTimeList[2].toString())
                        tieGapTime4.setText(it.gapTimeList[3].toString())
                        tieSoundEffectLoopTime.setText(it.soundEffectLoopTime.toString())
                    }
                }
            }
        }
    }
}
