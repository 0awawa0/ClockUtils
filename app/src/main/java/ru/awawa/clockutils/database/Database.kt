package ru.awawa.clockutils.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Alarm::class], version = 1, exportSchema = false)
abstract class Database: RoomDatabase() {
    abstract fun alarmDao(): AlarmDao
}