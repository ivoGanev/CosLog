package com.ifyezedev.coslog.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "cosplays", indices = [Index(value = ["cid"])])
data class Cosplay(
        @PrimaryKey(autoGenerate = true)
        val cid: Long = 0L,

        var name: String,

        var series: String,

        @ColumnInfo(name = "start_date")
        var startDate: Long,

        @ColumnInfo(name = "due_date")
        var dueDate: Long?,

        @ColumnInfo(name = "last_modified")
        var lastModified: Long,

        @ColumnInfo(name = "budget")
        var budget: Double?

)