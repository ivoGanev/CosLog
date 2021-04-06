package com.ifyezedev.coslog.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "process_items")
data class Process(

    @PrimaryKey(autoGenerate = true)
    val processId: Int,

    val cid: Int,

    @ColumnInfo(name = "action")
    var action: String,

    @ColumnInfo(name = "date")
    var date: Long


)
