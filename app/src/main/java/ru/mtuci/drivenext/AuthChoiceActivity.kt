package ru.mtuci.drivenext

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AuthChoiceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // установка темы приложения
        setTheme(R.style.Theme_DriveNext)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth_choice)

        val btnSignIn = findViewById<Button>(R.id.btnSignIn)
        val btnSignUp = findViewById<Button>(R.id.btnSignUp)

        btnSignIn.setOnClickListener {
            Toast.makeText(this, "Переход на экран входа", Toast.LENGTH_SHORT).show()
        }

        btnSignUp.setOnClickListener {
            Toast.makeText(this, "Переход на экран регистрации", Toast.LENGTH_SHORT).show()
        }
    }
}