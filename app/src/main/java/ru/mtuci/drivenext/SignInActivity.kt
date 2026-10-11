package ru.mtuci.drivenext

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Pattern

class SignInActivity : AppCompatActivity() {

    private lateinit var tilEmail: TextInputLayout
    private lateinit var etEmail: TextInputEditText
    private lateinit var tilPassword: TextInputLayout
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnSignInAction: Button
    private lateinit var btnGoogleSignIn: AppCompatButton
    private lateinit var tvGoToSignUp: TextView
    private lateinit var tvForgotPassword: TextView

    // паттерн валидации email: name@domainname.com или .ru
    private val emailPattern: Pattern = Pattern.compile(
        "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.(com|ru)$",
        Pattern.CASE_INSENSITIVE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_DriveNext)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        initViews()
        setupInputWatchers()
        setupListeners()
    }

    private fun initViews() {
        tilEmail = findViewById(R.id.tilEmail)
        etEmail = findViewById(R.id.etEmail)
        tilPassword = findViewById(R.id.tilPassword)
        etPassword = findViewById(R.id.etPassword)
        btnSignInAction = findViewById(R.id.btnSignInAction)
        btnGoogleSignIn = findViewById(R.id.btnGoogleSignIn)
        tvGoToSignUp = findViewById(R.id.tvGoToSignUp)
        tvForgotPassword = findViewById(R.id.tvForgotPassword)
    }

    private fun setupInputWatchers() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val email = etEmail.text.toString().trim()
                val password = etPassword.text.toString().trim()

                // кнопка войти активна только после заполнения всех полей
                btnSignInAction.isEnabled = email.isNotEmpty() && password.isNotEmpty()

                // сбрасываем ошибки при начале нового ввода
                tilEmail.error = null
                tilPassword.error = null
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        etEmail.addTextChangedListener(textWatcher)
        etPassword.addTextChangedListener(textWatcher)
    }

    private fun setupListeners() {
        // нажатие на кнопку войти
        btnSignInAction.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (!validateEmail(email)) {
                tilEmail.error = getString(R.string.error_invalid_email)
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                tilPassword.error = getString(R.string.error_empty_password)
                return@setOnClickListener
            }

            // имитация успешной авторизации и сохранение сессии
            performLoginSuccess(email)
        }

        // вход через Google
        btnGoogleSignIn.setOnClickListener {
            Toast.makeText(this, "Авторизация через Google...", Toast.LENGTH_SHORT).show()
            performLoginSuccess("google_user@drivenext.ru")
        }

        // забыли пароль
        tvForgotPassword.setOnClickListener {
            Toast.makeText(this, "Восстановление пароля временно недоступно", Toast.LENGTH_SHORT).show()
        }

        // переход на регистрацию
        tvGoToSignUp.setOnClickListener {
            Toast.makeText(this, "Переход на регистрацию", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateEmail(email: String): Boolean {
        return emailPattern.matcher(email).matches()
    }

    private fun performLoginSuccess(email: String) {
        // сохранение сессии в SharedPreferences
        val sharedPref = getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("is_logged_in", true)
            putString("user_email", email)
            apply()
        }

        Toast.makeText(this, "Успешный вход!", Toast.LENGTH_SHORT).show()

        // переход на экран приложения с очисткой стека авторизации
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }
}