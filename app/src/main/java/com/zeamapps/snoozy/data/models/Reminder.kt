package com.zeamapps.snoozy.data.models

import androidx.compose.ui.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zeamapps.snoozy.presentation.components.RepeatingOptions

@Entity(tableName = "reminders-table")
data class Reminder(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    @ColumnInfo(name = "tittle")
    var tittle: String,
    @ColumnInfo(name = "description")
    var description: String,
    @ColumnInfo(name = "tagcolor")
    var tagColor: Long,
    @ColumnInfo(name = "time")
    var time: Long,
    @ColumnInfo(name = "repeatingOption")
    var repeatingOptions : RepeatingOptions
)