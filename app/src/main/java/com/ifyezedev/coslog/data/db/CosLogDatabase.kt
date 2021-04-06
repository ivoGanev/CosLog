package com.ifyezedev.coslog.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ifyezedev.coslog.data.db.entities.Cosplay


@Database(
        entities = [Cosplay::class],
        version = 1

)
abstract class CosLogDatabase : RoomDatabase(){

        abstract fun getCosLogDao() : CosLogDao

}