package com.ifyezedev.coslog.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cosplay_items")
data class Cosplay(

        @PrimaryKey(autoGenerate = true)
        val cid: Int,

        @ColumnInfo(name = "name")
        var name: String,

        @ColumnInfo(name = "series")
        var series: String,

        @ColumnInfo(name = "start_date")
        var startDate: Long,

        @ColumnInfo(name = "due_date")
        var dueDate: Long,

        @ColumnInfo(name = "last_modified")
        var lastModified: Long,

        @ColumnInfo(name = "budget")
        var budget: Double

)