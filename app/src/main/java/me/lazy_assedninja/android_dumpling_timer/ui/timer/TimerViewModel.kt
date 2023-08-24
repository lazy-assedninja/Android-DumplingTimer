package me.lazy_assedninja.android_dumpling_timer.ui.timer

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import me.lazy_assedninja.android_dumpling_timer.data.db.Setting
import me.lazy_assedninja.android_dumpling_timer.data.repository.SettingRepository
import me.lazy_assedninja.android_dumpling_timer.data.vo.Time
import timber.log.Timber

class TimerViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SettingRepository

    private lateinit var firepowerList: MutableList<Float>
    val list1 = mutableListOf<Time>()
    val list2 = mutableListOf<Time>()
    val list3 = mutableListOf<Time>()
    val list4 = mutableListOf<Time>()

    var setting: Setting? = null

    private val _initSettingFinished = MutableSharedFlow<Unit>()
    val initSettingFinished: SharedFlow<Unit> = _initSettingFinished

    private var currentTimerList = mutableListOf<Int>() // 用於返回上一步時判斷最新新增之List

    init {
        repository = SettingRepository(application)
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

                _initSettingFinished.emit(Unit)

                Timber.tag("xxxxx").d("firepowerList: $firepowerList")
            }
        }
    }

    fun addData(listID: Int) {
        currentTimerList.add(listID)

        when (listID) {
            1 -> list2
            2 -> list3
            3 -> list4
            else -> list1
        }.add(Time(listID + 1, (setting?.baseTime ?: 0).toDouble()))
    }

    fun doneData(listID: Int) = when (listID) {
        1 -> list2
        2 -> list3
        3 -> list4
        else -> list1
    }.apply {
        currentTimerList.removeFirst()
    }.removeFirst()

    fun revertData() = if (currentTimerList.isEmpty()) -1 else currentTimerList.removeLast().apply {
        when (this) {
            1 -> list2
            2 -> list3
            3 -> list4
            else -> list1
        }.removeLast()
    }

    fun onTick(listID: Int) {
        when (listID) {
            1 -> list2
            2 -> list3
            3 -> list4
            else -> list1
        }.forEachIndexed { index, time ->
            Timber.tag("yyy").d("onTick: ${time.percentage}")
            time.percentage -= firepowerList[index]
        }
    }
}
