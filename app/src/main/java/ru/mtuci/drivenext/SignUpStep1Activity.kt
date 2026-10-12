package ru.mtuci.drivenext

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Pattern

class SignUpStep1Activity : AppCompatActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var tilEmail: TextInputLayout
    private lateinit var etEmail: TextInputEditText
    private lateinit var tilPassword: TextInputLayout
    private lateinit var etPassword: TextInputEditText
    private lateinit var tilRepeatPassword: TextInputLayout
    private lateinit var etRepeatPassword: TextInputEditText
    private lateinit var cbTerms: CheckBox
    private lateinit var btnNext: Button

    // паттерн email: name@domainname.com или .ru
    private val emailPattern: Pattern = Pattern.compile(
        "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.(com|ru)$",
        Pattern.CASE_INSENSITIVE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_DriveNext)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_step1)

        initViews()
        setupTextWatchers()
        setupListeners()
    }

    private fun initViews() {
        btnBack = findViewById(R.id.btnBack)
        tilEmail = findViewById(R.id.tilEmail)
        etEmail = findViewById(R.id.etEmail)
        tilPassword = findViewById(R.id.tilPassword)
        etPassword = findViewById(R.id.etPassword)
        tilRepeatPassword = findViewById(R.id.tilRepeatPassword)
        etRepeatPassword = findViewById(R.id.etRepeatPassword)
        cbTerms = findViewById(R.id.cbTerms)
        btnNext = findViewById(R.id.btnNext)
    }

    private fun setupTextWatchers() {
        val clearErrorWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tilEmail.error = null
                tilPassword.error = null
                tilRepeatPassword.error = null
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        etEmail.addTextChangedListener(clearErrorWatcher)
        etPassword.addTextChangedListener(clearErrorWatcher)
        etRepeatPassword.addTextChangedListener(clearErrorWatcher)
    }

    private fun setupListeners() {
        // кнопка назад: возвращает пользователя на предыдущий экран
        btnBack.setOnClickListener {
            finish()
        }

        // кнопка далее
        btnNext.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString()
            val repeatPassword = etRepeatPassword.text.toString()

            // проверка email
            if (!emailPattern.matcher(email).matches()) {
                tilEmail.error = getString(R.string.error_invalid_email)
                return@setOnClickListener
            }

            // проверка пустого пароля
            if (password.isEmpty()) {
                tilPassword.error = getString(R.string.error_empty_password)
                return@setOnClickListener
            }

            // проверка совпадения паролей
            if (password != repeatPassword) {
                tilRepeatPassword.error = getString(R.string.error_passwords_mismatch)
                return@setOnClickListener
            }

            // проверка обязательного чекбокса условий
            if (!cbTerms.isChecked) {
                Toast.makeText(
                    this,
                    getString(R.string.error_terms_required),
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            // успешная валидация: передаем введенные данные дальше
            Toast.makeText(this, "Шаг 1 пройден", Toast.LENGTH_SHORT).show()
            // успешная валидация: открываем шаг 2 и передаем email и пароль
            val intent = Intent(this, SignUpStep2Activity::class.java).apply {
                putExtra("extra_email", email)
                putExtra("extra_password", password)
            }
            startActivity(intent)
        }
    }
}