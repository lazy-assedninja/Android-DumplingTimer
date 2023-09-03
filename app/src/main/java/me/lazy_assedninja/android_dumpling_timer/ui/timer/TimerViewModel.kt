package me.lazy_assedninja.android_dumpling_timer.ui.timer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.lazy_assedninja.android_dumpling_timer.data.db.Setting
import me.lazy_assedninja.android_dumpling_timer.data.repository.SettingRepository
import me.lazy_assedninja.android_dumpling_timer.data.vo.Time
import timber.log.Timber

class TimerViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SettingRepository(application)

    var setting: Setting? = null
    private lateinit var firepowerList: MutableList<Float>

    var currentTimerList = mutableListOf<Int>() // 用於返回上一步時判斷最新新增之List

    private val _initSettingFinished = MutableSharedFlow<Unit>()
    val initSettingFinished: SharedFlow<Unit> = _initSettingFinished

    private val _list1UiState = MutableStateFlow<ListUiState>(ListUiState.Init)
    val list1UiState: StateFlow<ListUiState> = _list1UiState
    val cacheList1 = mutableListOf<Time>()

    private val _list2UiState = MutableStateFlow<ListUiState>(ListUiState.Init)
    val list2UiState: StateFlow<ListUiState> = _list2UiState
    val cacheList2 = mutableListOf<Time>()

    private val _list3UiState = MutableStateFlow<ListUiState>(ListUiState.Init)
    val list3UiState: StateFlow<ListUiState> = _list3UiState
    val cacheList3 = mutableListOf<Time>()

    private val _list4UiState = MutableStateFlow<ListUiState>(ListUiState.Init)
    val list4UiState: StateFlow<ListUiState> = _list4UiState
    val cacheList4 = mutableListOf<Time>()

    init {
        viewModelScope.launch {
            repository.getSetting().collect {
                setting = it

                firepowerList = mutableListOf(1F)
                setting?.gapTimeList?.forEachIndexed { index, _ ->
                    var gap = 0F
                    for (i in 0..index) {
                        gap += firepowerList[i] * (setting?.gapTimeList?.get(index - i) ?: 0)
                    }
                    firepowerList.add(((setting?.baseTime ?: 0) - gap) / (setting?.baseTime ?: 0))
                }

                Timber.tag("xxxxx").d("setting: $setting")
                _initSettingFinished.emit(Unit)
            }
        }
    }

    fun addData(listID: Int) = (currentTimerList.filter { it == listID }.size < 5).apply {
        if (this) {
            currentTimerList.add(listID)

            when (listID) {
                1 -> _list2UiState
                2 -> _list3UiState
                3 -> _list4UiState
                else -> _list1UiState
            }.apply {
                value = ListUiState.TimeList(
                    (if (value is ListUiState.Init) emptyList() else (value as ListUiState.TimeList).list).toMutableList().apply {
                        add(Time(listID + 1, (setting?.baseTime ?: 0).toDouble()))
                    })
            }
        }
    }

    fun doneData(listID: Int): Time {
        val time: Time
        when (listID) {
            1 -> _list2UiState
            2 -> _list3UiState
            3 -> _list4UiState
            else -> _list1UiState
        }.apply {
            value = ListUiState.TimeList(
                (if (value is ListUiState.Init) emptyList() else (value as ListUiState.TimeList).list).toMutableList().apply {
                    currentTimerList.remove(listID)
                    time = removeFirst()
                })
        }
        return time
    }

    fun revertData() {
        when (currentTimerList.removeLast()) {
            1 -> _list2UiState
            2 -> _list3UiState
            3 -> _list4UiState
            else -> _list1UiState
        }.apply {
            value = ListUiState.TimeList(
                (if (value is ListUiState.Init) emptyList() else (value as ListUiState.TimeList).list).toMutableList().apply {
                    removeLast()
                })
        }
    }

    fun onTick(listID: Int) {
        when (listID) {
            1 -> _list2UiState
            2 -> _list3UiState
            3 -> _list4UiState
            else -> _list1UiState
        }.apply {
            val list = (if (value is ListUiState.Init) emptyList() else (value as ListUiState.TimeList).list).toMutableList().apply {
                for (index in 0..lastIndex) {
                    val time = this[index]
                    this[index] = time.copy(id = time.id, percentage = time.percentage - firepowerList[index])
                }
            }
            Timber.tag("xxxxx").d("onTick: $list")
            value = ListUiState.TimeList(list)
        }
    }
}

sealed class ListUiState {
    data object Init : ListUiState()
    data class TimeList(val list: List<Time>) : ListUiState()
}