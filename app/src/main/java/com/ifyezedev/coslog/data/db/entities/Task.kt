package com.ifyezedev.coslog.data.db.entities

import androidx.room.*
//, foreignKeys = [ForeignKey(entity = Cosplay::class, parentColumns = ["cid"], childColumns = ["cosplay_id"])],
//indices = [Index(value = ["cosplay_id"])]
@Entity(tableName = "task")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val taskId: Int,

    val cosplay_id: Int,

    var name: String,

    var reminderDate: Long,

    var notes: String


)
