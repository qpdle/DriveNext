package ru.mtuci.drivenext

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    private lateinit var ivProfileAvatar: ImageView
    private lateinit var btnLogout: TextView

    // Контракт для выбора одного фото из галереи по ТЗ
    private val pickAvatarLauncher = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            ivProfileAvatar.setImageURI(uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_DriveNext)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        ivProfileAvatar = findViewById(R.id.ivProfileAvatar)
        btnLogout = findViewById(R.id.btnLogout)

        // Запуск выбора фото
        ivProfileAvatar.setOnClickListener {
            pickAvatarLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }

        // Логика выхода из аккаунта: очистка SharedPreferences и переход в SignInActivity
        btnLogout.setOnClickListener {
            val sharedPref = getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
            sharedPref.edit().putBoolean("is_logged_in", false).apply()

            val intent = Intent(this, SignInActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            finish()
        }
    }
}