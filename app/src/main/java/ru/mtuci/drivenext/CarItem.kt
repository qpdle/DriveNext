package ru.mtuci.drivenext

data class CarItem(
    val id: String,
    val brand: String,
    val model: String,
    val pricePerDay: Int,
    val transmission: String,
    val fuelType: String,
    val imageResId: Int = R.drawable.ic_splash_logo
)