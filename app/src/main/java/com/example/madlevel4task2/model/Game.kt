package com.example.madlevel4task2.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gameTable")
data class Game(
        @PrimaryKey(autoGenerate = true)
        val id: Long? = null,

        var dateTime: String,

        var moveComputer: Int,

        var movePlayer: Int,

        var result: Int
)