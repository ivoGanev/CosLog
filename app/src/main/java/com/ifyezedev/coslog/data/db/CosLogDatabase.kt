package com.ifyezedev.coslog.data.db

import android.content.Context
import android.util.Log
import androidx.room.*
import com.ifyezedev.coslog.data.db.entities.*



@Database(
        entities = [
                Cosplay::class,
                Element::class,
                Note::class,
                Task::class,
                Process::class,
                Picture::class,
                Event::class
                   ],
        version = 1,
        exportSchema = false)
@TypeConverters(Converters::class)
abstract class CosLogDatabase : RoomDatabase() {

        abstract val cosLogDao: CosLogDao

        companion object {

                @Volatile
                private var INSTANCE: CosLogDatabase? = null

                fun getDatabase(context: Context): CosLogDatabase {
                        synchronized(this) {
                                var instance = INSTANCE

                                if (instance == null) {
                                        instance = Room.databaseBuilder(
                                                context.applicationContext,
                                                CosLogDatabase::class.java,
                                                "coslog_database"
                                        )
                                                .fallbackToDestructiveMigration()
                                                .build()
                                        INSTANCE = instance
                                }

                                Log.i("database", "database created")

                                return instance
                        }
                }
        }
}
