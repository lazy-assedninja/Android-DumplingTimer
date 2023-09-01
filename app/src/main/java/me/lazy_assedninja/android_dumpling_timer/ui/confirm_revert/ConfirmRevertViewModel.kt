package me.lazy_assedninja.android_dumpling_timer.ui.confirm_revert

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class ConfirmRevertViewModel : ViewModel() {

    private val _confirmClick = MutableSharedFlow<Unit>()
    val confirmClick: SharedFlow<Unit> = _confirmClick

    fun confirmClick() {
        viewModelScope.launch {
            _confirmClick.emit(Unit)
        }
    }
}