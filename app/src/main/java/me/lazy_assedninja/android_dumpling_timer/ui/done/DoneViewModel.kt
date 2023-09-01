package me.lazy_assedninja.android_dumpling_timer.ui.done

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import me.lazy_assedninja.android_dumpling_timer.data.repository.SettingRepository
import me.lazy_assedninja.android_dumpling_timer.data.vo.Time

class DoneViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SettingRepository(application)

    val setting = repository.getSetting().shareIn(viewModelScope, SharingStarted.Eagerly)

    private val _uiState = MutableStateFlow<UiState>(UiState.DoneList(emptyList()))
    val uiState: StateFlow<UiState> = _uiState

    fun addData(item: Time) {
        viewModelScope.launch {
            _uiState.value = UiState.DoneList((_uiState.value as UiState.DoneList).list.toMutableList().apply {
                add(item)
            })
        }
    }

    fun removeData(item: Time) {
        viewModelScope.launch {
            if (_uiState.value is UiState.DoneList) _uiState.value =
                UiState.DoneList((_uiState.value as UiState.DoneList).list.toMutableList().apply {
                    remove(item)
                })
        }
    }
}

sealed class UiState {
    data class DoneList(val list: List<Time>) : UiState()
}
