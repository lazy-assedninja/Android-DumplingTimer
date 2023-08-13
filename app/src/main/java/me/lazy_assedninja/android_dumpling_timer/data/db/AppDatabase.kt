package me.lazy_assedninja.android_dumpling_timer.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * The Room database for this app
 */
@Database(entities = [Setting::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun settingDao(): SettingDao

    companion object {

        private const val DATABASE_NAME = "dumpling_timer"

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME).build()
    }
}