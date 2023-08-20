package me.lazy_assedninja.android_dumpling_timer.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import me.lazy_assedninja.android_dumpling_timer.data.db.Setting
import me.lazy_assedninja.android_dumpling_timer.data.repository.SettingRepository

class SettingViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SettingRepository

    init {
        repository = SettingRepository(application)
        viewModelScope.launch {
            repository.getSetting().collect {
                _setting.emit(it)
            }
        }
    }

    private val _setting: MutableSharedFlow<Setting?> = MutableSharedFlow()
    val setting: SharedFlow<Setting?> = _setting

    fun setSetting(baseTime: Long, gapTime: Long, soundEffectTime: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.setSetting(baseTime, gapTime, soundEffectTime)
        }
    }
}