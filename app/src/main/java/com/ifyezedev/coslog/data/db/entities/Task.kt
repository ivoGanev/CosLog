package com.ifyezedev.coslog.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "task_items", foreignKeys = [ForeignKey(entity = Cosplay::class, parentColumns = ["cid"], childColumns = ["cosplay_id"])])
data class Task(
    @PrimaryKey(autoGenerate = true)
    val taskId: Int,

    val cosplay_id: Int,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "reminder_date")
    var reminderDate: Long,

    @ColumnInfo(name = "notes")
    var notes: String


)
