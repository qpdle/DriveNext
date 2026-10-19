package ru.mtuci.drivenext

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_DriveNext)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val layoutProfileHeader = findViewById<ConstraintLayout>(R.id.layoutProfileHeader)
        layoutProfileHeader.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        val tabHome = findViewById<LinearLayout>(R.id.tabHome)
        tabHome.setOnClickListener {
            // При нажатии на Главную возвращаемся назад, закрывая Настройки
            finish()
        }
    }
}