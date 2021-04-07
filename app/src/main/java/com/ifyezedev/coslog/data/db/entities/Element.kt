package com.ifyezedev.coslog.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(tableName = "element_items", foreignKeys = [ForeignKey(entity = Cosplay::class, parentColumns = ["cid"], childColumns = ["cosplay_id"])])
data class Element(
    @PrimaryKey(autoGenerate = true)
    val eid: Int,

    val cosplay_id: Int,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "is_buy")
    var isBuy: Boolean,

    @ColumnInfo(name = "cost")
    var cost: Double,

    @ColumnInfo(name = "time")
    var time: Long,

    @ColumnInfo(name = "source")
    var source: String,

    @ColumnInfo(name = "notes")
    var notes: String

)