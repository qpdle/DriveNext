package ru.mtuci.drivenext

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SignUpSuccessActivity : AppCompatActivity() {

    private lateinit var btnGoToMain: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_DriveNext)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_success)

        // сохраняем флаг успешной авторизации в SharedPreferences
        val sharedPref = getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("is_logged_in", true)
            apply()
        }

        // обработка клика перехода на главный экран
        btnGoToMain = findViewById(R.id.btnGoToMain)
        btnGoToMain.setOnClickListener {
            // переход в MainActivity с полной очисткой стека авторизации
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            finish()
        }
    }
}