package com.ifyezedev.coslog.data.db.entities

import androidx.room.*
//, foreignKeys = [ForeignKey(entity = Cosplay::class, parentColumns = ["cid"], childColumns = ["cosplay_id"])],
//indices = [Index(value = ["cosplay_id"])]
@Entity(tableName = "pictures")
data class Picture(

    @PrimaryKey(autoGenerate = true)
    val pictureId: Int,

    val cosplay_id: Int,

    var name: String,

    var isReference: Boolean,

    var notes: String

)
