package ru.awawa.clockutils.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Alarm(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "time") var time: Int,
    @ColumnInfo(name = "repeat") var repeat: String,
    @ColumnInfo(name = "ringtone") var ringtone: String,
    @ColumnInfo(name = "vibrate") var vibrate: Boolean,
    @ColumnInfo(name = "label") var label: String
)