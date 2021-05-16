package ru.awawa.clockutils.database

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface AlarmDao {

    @Query("SELECT * FROM Alarm")
    fun getAllAlarms(): Flow<List<Alarm>>
}