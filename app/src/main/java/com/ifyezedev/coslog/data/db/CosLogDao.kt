package com.ifyezedev.coslog.data.db

import androidx.room.*
import com.ifyezedev.coslog.data.db.entities.*

@Dao
interface CosLogDao {
    @Insert
    suspend fun insertCosplay(cosplay: Cosplay)

    @Insert
    suspend fun insertElement(element: Element)

    @Insert
    suspend fun insertPicture(picture: Picture)

    @Insert
    suspend fun insertProcess(process: Process)

    @Insert
    suspend fun insertTask(task: Task)

    @Insert
    suspend fun insertNote(note: Note)

    @Insert
    suspend fun insertEvent(event: Event)

    @Update
    suspend fun updateCosplay(cosplay: Cosplay)

    @Update
    suspend fun updateElement(element: Element)

    @Update
    suspend fun updateNote(note: Note)

    @Update
    suspend fun updateTask(task: Task)

    @Update
    suspend fun updatePicture(picture: Picture)

    @Update
    suspend fun updateEvent(event: Event)

    @Update
    suspend fun updateProcess(process: Process)

    @Query("SELECT * FROM cosplays")
    suspend fun getAllCosplays() : List<Cosplay>

    @Query("SELECT * FROM elements")
    suspend fun getAllElements() : List<Element>

    @Delete
    suspend fun deleteElement(element: Element)
}
