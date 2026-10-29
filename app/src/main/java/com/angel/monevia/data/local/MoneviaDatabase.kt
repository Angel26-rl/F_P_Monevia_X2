package com.angel.monevia.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [TransaccionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MoneviaDatabase : RoomDatabase() {

    abstract fun transaccionDao(): TransaccionDao

    companion object {
        @Volatile
        private var INSTANCE: MoneviaDatabase? = null

        fun obtenerInstancia(context: Context): MoneviaDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    MoneviaDatabase::class.java,
                    "monevia_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
