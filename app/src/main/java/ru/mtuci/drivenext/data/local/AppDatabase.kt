package ru.mtuci.drivenext.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.mtuci.drivenext.data.local.dao.BookingDao
import ru.mtuci.drivenext.data.local.dao.FavoriteCarDao
import ru.mtuci.drivenext.data.local.entity.BookingEntity
import ru.mtuci.drivenext.data.local.entity.FavoriteCarEntity

@Database(
    entities = [FavoriteCarEntity::class, BookingEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoriteCarDao(): FavoriteCarDao
    abstract fun bookingDao(): BookingDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}