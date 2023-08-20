package me.lazy_assedninja.android_dumpling_timer.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.lazy_assedninja.android_dumpling_timer.data.db.Setting
import me.lazy_assedninja.android_dumpling_timer.data.repository.SettingRepository
import me.lazy_assedninja.android_dumpling_timer.data.vo.Time
import timber.log.Timber

class TimerViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SettingRepository

    val list1 = mutableListOf<Time>()
    val list2 = mutableListOf<Time>()
    val list3 = mutableListOf<Time>()
    val list4 = mutableListOf<Time>()

    init {
        repository = SettingRepository(application)
        viewModelScope.launch {
            repository.getSetting().collect {
                setting = it
            }
        }
    }

    var setting: Setting? = null

    private var currentTimerList = 0 // 用於返回上一步時判斷最新新增之List

    fun addData(listID: Int) {
        currentTimerList = listID
        val list = when (currentTimerList) {
            1 -> list2
            2 -> list3
            3 -> list4
            else -> list1
        }

        Timber.tag("xxxxx").d("setting: $setting, baseTime: ${setting?.baseTime}, extra time: ${(setting?.gapTime ?: 0) * (list.size)}")

        list.add(Time(listID + 1, (setting?.baseTime ?: 0) + (setting?.gapTime ?: 0) * (list.size)))
    }

    fun revertData(): Int {
        val list = when (currentTimerList) {
            1 -> list2
            2 -> list3
            3 -> list4
            else -> list1
        }
        if (list.isNotEmpty()) list.removeLast()

        return currentTimerList
    }
}