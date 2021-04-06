package com.ifyezedev.coslog.data.db

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.ifyezedev.coslog.data.db.entities.*
import com.ifyezedev.coslog.data.db.entities.relations.*

interface CosLogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCosplay(cosplay: Cosplay)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertElement(element: Element)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: Event)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPicture(picture: Picture)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProcess(task: Task)

    @Transaction
    @Query("SELECT * FROM cosplay_items WHERE cid = :cid")
    suspend fun getCosplayAndElementWithCid(cid: Int): List<CosplayAndElement>

    @Transaction
    @Query("SELECT * FROM cosplay_items WHERE cid = :cid")
    suspend fun getCosplayAndEventWithCid(cid: Int): List<CosplayAndEvent>

    @Transaction
    @Query("SELECT * FROM cosplay_items WHERE cid = :cid")
    suspend fun getCosplayAndNoteWithCid(cid: Int): List<CosplayAndNote>

    @Transaction
    @Query("SELECT * FROM cosplay_items WHERE cid = :cid")
    suspend fun getCosplayAndPictureWithCid(cid: Int): List<CosplayAndPicture>

    @Transaction
    @Query("SELECT * FROM cosplay_items WHERE cid = :cid")
    suspend fun getCosplayAndProcessWithCid(cid: Int): List<CosplayAndProcess>

    @Transaction
    @Query("SELECT * FROM cosplay_items WHERE cid = :cid")
    suspend fun getCosplayAndTaskWithCid(cid: Int): List<CosplayAndTask>

}