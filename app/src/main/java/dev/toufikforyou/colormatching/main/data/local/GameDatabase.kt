package dev.toufikforyou.colormatching.main.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.toufikforyou.colormatching.main.data.local.dao.GameProgressDao
import dev.toufikforyou.colormatching.main.data.local.entity.GameProgress

@Database(entities = [GameProgress::class], version = 1)
abstract class GameDatabase : RoomDatabase() {
    abstract fun gameProgressDao(): GameProgressDao

    companion object {
        @Volatile
        private var INSTANCE: GameDatabase? = null

        fun getDatabase(context: Context): GameDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GameDatabase::class.java,
                    "game_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
} 