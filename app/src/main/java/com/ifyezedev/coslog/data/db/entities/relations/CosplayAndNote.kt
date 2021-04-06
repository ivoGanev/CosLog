package com.ifyezedev.coslog.data.db.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.ifyezedev.coslog.data.db.entities.Cosplay
import com.ifyezedev.coslog.data.db.entities.Note

data class CosplayAndNote(
    @Embedded val cosplay: Cosplay,
    @Relation(
        parentColumn = "cid",
        entityColumn = "cid"
    )
    val note: Note
)
