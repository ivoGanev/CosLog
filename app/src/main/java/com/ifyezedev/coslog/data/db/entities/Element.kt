package com.ifyezedev.coslog.data.db.entities

import androidx.room.*
import com.ifyezedev.coslog.data.db.entities.*
import androidx.room.ForeignKey.CASCADE

@Entity(tableName = "elements")
data class Element(
    @PrimaryKey(autoGenerate = true)
    val eid: Long = 0L,

    val cosplay_id: Long,

    var name: String,

    @ColumnInfo(name = "is_buy")
    var isBuy: Boolean,

    var cost: Double,

    var time: Long,

    var source: String,

    var notes: String
)