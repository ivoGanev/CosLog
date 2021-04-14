package com.ifyezedev.coslog.data.db.entities

import androidx.room.*

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val noteId: Long = 0L,

    val cosplay_id: Long,

    var title: String,

    var body: String

)
