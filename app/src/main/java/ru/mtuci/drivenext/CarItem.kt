package ru.mtuci.drivenext

import java.io.Serializable

data class CarItem(
    val id: String,
    val brand: String,
    val model: String,
    val pricePerDay: Int,
    val transmission: String,
    val fuelType: String,
    val imageResId: Int = R.drawable.ic_splash_logo,
    val address: String = "Авиамоторная ул., 8, стр. 2",
    val description: String = "Автомобиль оснащен современным двигателем, передовой мультимедиа и системами активной безопасности для комфортной аренды."
) : Serializable