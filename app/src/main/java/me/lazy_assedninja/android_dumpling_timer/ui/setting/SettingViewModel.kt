package me.lazy_assedninja.android_dumpling_timer.ui.setting

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import me.lazy_assedninja.android_dumpling_timer.data.db.Setting
import me.lazy_assedninja.android_dumpling_timer.data.repository.SettingRepository

class SettingViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SettingRepository(application)

    val setting: SharedFlow<Setting?> = repository.getSetting().shareIn(viewModelScope, SharingStarted.Eagerly)

    private val _uiState = MutableStateFlow<UiState>(UiState.Init)
    val uiState: StateFlow<UiState> = _uiState

    init {
        viewModelScope.launch {
            setting.collect {
                it?.let { setting ->
                    _uiState.value = UiState.SettingData(setting)
                }
            }
        }
    }

    fun setSetting(baseTime: Long, gapTimeList: List<Long>, soundEffectLoopTime: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.setSetting(baseTime, gapTimeList, soundEffectLoopTime)
        }
    }
}

sealed class UiState {
    data object Init : UiState()
    data class SettingData(val setting: Setting) : UiState()
}
