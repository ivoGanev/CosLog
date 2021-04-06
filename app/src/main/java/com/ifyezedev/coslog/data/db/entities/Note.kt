package com.ifyezedev.coslog.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_items")
data class Note(


    @PrimaryKey(autoGenerate = true)
    val noteId: Int,

    val cid: Int,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "body")
    var body: String

)
