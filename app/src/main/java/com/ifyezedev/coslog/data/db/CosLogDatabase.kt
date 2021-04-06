package com.ifyezedev.coslog.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ifyezedev.coslog.data.db.entities.*


@Database(
        entities = [
                Cosplay::class,
                Element::class,
                Event::class,
                Note::class,
                Picture::class,
                Process::class,
                Task::class
        ],
        version = 1

)
abstract class CosLogDatabase : RoomDatabase(){

        abstract fun getCosLogDao() : CosLogDao

        companion object {
                @Volatile
                private var INSTANCE: CosLogDatabase? = null

                fun getInstance(context: Context): CosLogDatabase {
                        synchronized(this){
                                return INSTANCE ?: Room.databaseBuilder(
                                        context.applicationContext,
                                        CosLogDatabase::class.java,
                                        "cosplay_db"
                                ).build().also{
                                        INSTANCE = it
                                }
                        }
                }


        }

}