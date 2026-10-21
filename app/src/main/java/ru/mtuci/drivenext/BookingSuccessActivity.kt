package ru.mtuci.drivenext

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class BookingSuccessActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_DriveNext)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_success)

        val btnToMyBookings: Button = findViewById(R.id.btnToMyBookings)
        val btnBackHome: AppCompatButton = findViewById(R.id.btnBackHome)

        // Переход на экран списка бронирований (реализуется на Шаге 4)
        btnToMyBookings.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                putExtra("open_my_bookings", true)
            }
            startActivity(intent)
            finish()
        }

        // Возврат на главный экран с очисткой стека
        btnBackHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            finish()
        }
    }
}