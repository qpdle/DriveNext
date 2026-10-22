package ru.mtuci.drivenext

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_DriveNext)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Переход в Профиль по клику на плашку пользователя
        val layoutProfileHeader = findViewById<ConstraintLayout>(R.id.layoutProfileHeader)
        layoutProfileHeader.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        // Переход в Мои бронирования по клику на пункт меню
        val tvMyBookingsItem = findViewById<TextView>(R.id.tvMyBookingsItem)
        tvMyBookingsItem.setOnClickListener {
            val intent = Intent(this, MyBookingsActivity::class.java)
            startActivity(intent)
        }

        // Возврат на Главную при нажатии на иконку в нижнем меню
        val tabHome = findViewById<LinearLayout>(R.id.tabHome)
        tabHome.setOnClickListener {
            finish()
        }
    }
}