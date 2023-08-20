package me.lazy_assedninja.android_dumpling_timer.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Setting(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "base_time") val baseTime: Long,
    @ColumnInfo(name = "gap_time") val gapTime: Long,
    @ColumnInfo(name = "sound_effect_time") val soundEffectTime: Long
)