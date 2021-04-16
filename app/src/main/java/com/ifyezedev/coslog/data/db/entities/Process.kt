package com.ifyezedev.coslog.data.db.entities

import androidx.room.*

@Entity(tableName = "process")
data class Process(

    @PrimaryKey(autoGenerate = true)
    val processId: Long = 0L,

    val cosplay_id: Long,

    var action: String,

    var date: Long


)
