package com.ifyezedev.coslog.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "process_items", foreignKeys = [ForeignKey(entity = Cosplay::class, parentColumns = ["cid"], childColumns = ["cosplay_id"])])
data class Process(

    @PrimaryKey(autoGenerate = true)
    val processId: Int,

    val cosplay_id: Int,

    @ColumnInfo(name = "action")
    var action: String,

    @ColumnInfo(name = "date")
    var date: Long


)
