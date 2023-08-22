package me.lazy_assedninja.android_dumpling_timer.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

@Dao
interface SettingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(setting: Setting)

    @Query("SELECT * FROM setting WHERE id = :id")
    fun get(id: Long): Flow<Setting?>
}
