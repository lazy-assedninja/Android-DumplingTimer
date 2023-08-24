package me.lazy_assedninja.android_dumpling_timer.ui.done

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import me.lazy_assedninja.android_dumpling_timer.data.repository.SettingRepository

class DoneViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SettingRepository(application)

    val setting = repository.getSetting().shareIn(viewModelScope, SharingStarted.Eagerly)
}