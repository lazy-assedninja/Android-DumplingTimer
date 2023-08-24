package me.lazy_assedninja.android_dumpling_timer.ui.setting

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import me.lazy_assedninja.android_dumpling_timer.data.db.Setting
import me.lazy_assedninja.android_dumpling_timer.data.repository.SettingRepository

class SettingViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SettingRepository(application)

    val setting: SharedFlow<Setting?> = repository.getSetting().shareIn(viewModelScope, SharingStarted.Eagerly)

    fun setSetting(baseTime: Long, gapTimeList: List<Long>, soundEffectLoopTime: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.setSetting(baseTime, gapTimeList, soundEffectLoopTime)
        }
    }
}
