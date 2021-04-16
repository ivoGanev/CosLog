package com.ifyezedev.coslog.data.db.entities

import androidx.room.*

@Entity(tableName = "events")
data class Event(
    @PrimaryKey(autoGenerate = true)
    val eventId: Long = 0L,

    val cosplay_id: Long,

    var name: String,

    var place: String,

    var date: Long,

    var notes: String
)
