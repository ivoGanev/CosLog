package com.ifyezedev.coslog.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "picture_items")
data class Picture(

    @PrimaryKey(autoGenerate = true)
    val pictureId: Int,

    val cid: Int,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "isReference")
    var isReference: Boolean,

    @ColumnInfo(name = "notes")
    var notes: String

)
