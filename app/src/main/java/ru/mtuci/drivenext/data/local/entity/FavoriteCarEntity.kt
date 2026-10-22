package ru.mtuci.drivenext.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_cars")
data class FavoriteCarEntity(
    @PrimaryKey
    val id: String,
    val brand: String,
    val model: String,
    val pricePerDay: Int,
    val transmission: String,
    val fuelType: String,
    val imageResId: Int,
    val address: String = "Авиамоторная ул., 8, стр. 2",
    val description: String = ""
)