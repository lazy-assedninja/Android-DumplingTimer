package me.lazy_assedninja.android_dumpling_timer.data.repository

import android.content.Context
import me.lazy_assedninja.android_dumpling_timer.data.db.Setting
import me.lazy_assedninja.android_dumpling_timer.data.db.AppDatabase
import me.lazy_assedninja.android_dumpling_timer.data.db.SettingDao

class SettingRepository(private val context: Context, private val dao: SettingDao = AppDatabase.getInstance(context).settingDao()) {

    fun setSetting(baseTime: Int, gapTime: Int, soundEffectTime: Int) {
        dao.insert(Setting(0, baseTime, gapTime, soundEffectTime))
    }

    fun getSetting() = dao.get(0)
}