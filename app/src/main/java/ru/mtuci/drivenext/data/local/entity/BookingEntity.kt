package ru.mtuci.drivenext.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey
    val bookingNumber: String,
    val carModel: String,
    val carBrand: String,
    val pricePerDay: Int,
    val address: String,
    val startDate: String,
    val endDate: String,
    val driverName: String,
    val driverLicense: String,
    val status: String, // "Одобрено" или "Завершена"
    val daysCount: Int,
    val rentPriceTotal: Int,
    val insuranceTotal: Int,
    val deposit: Int,
    val totalPrice: Int,
    val imageResId: Int
)