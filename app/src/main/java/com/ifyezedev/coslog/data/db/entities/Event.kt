package com.ifyezedev.coslog.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "event_items", foreignKeys = [ForeignKey(entity = Cosplay::class, parentColumns = ["cid"], childColumns = ["cosplay_id"])])
data class Event(
    @PrimaryKey(autoGenerate = true)
    val eventId: Int,

    val cosplay_id: Int,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "place")
    var place: String,

    @ColumnInfo(name = "date")
    var date: Long,

    @ColumnInfo(name = "notes")
    var notes: String
)
