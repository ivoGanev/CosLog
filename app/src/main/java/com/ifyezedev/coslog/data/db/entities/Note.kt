package com.ifyezedev.coslog.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "note_items", foreignKeys = [ForeignKey(entity = Cosplay::class, parentColumns = ["cid"], childColumns = ["cosplay_id"])])
data class Note(
    @PrimaryKey(autoGenerate = true)
    val noteId: Int,

    val cosplay_id: Int,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "body")
    var body: String

)
