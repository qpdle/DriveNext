package ru.mtuci.drivenext.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.mtuci.drivenext.data.local.entity.FavoriteCarEntity

@Dao
interface FavoriteCarDao {

    @Query("SELECT * FROM favorite_cars")
    suspend fun getAllFavorites(): List<FavoriteCarEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_cars WHERE id = :carId)")
    suspend fun isFavorite(carId: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(car: FavoriteCarEntity)

    @Query("DELETE FROM favorite_cars WHERE id = :carId")
    suspend fun deleteFavoriteById(carId: String)
}