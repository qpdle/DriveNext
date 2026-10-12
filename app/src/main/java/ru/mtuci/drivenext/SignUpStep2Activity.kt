package ru.mtuci.drivenext

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SignUpStep2Activity : AppCompatActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var tilLastName: TextInputLayout
    private lateinit var etLastName: TextInputEditText
    private lateinit var tilFirstName: TextInputLayout
    private lateinit var etFirstName: TextInputEditText
    private lateinit var tilPatronymic: TextInputLayout
    private lateinit var etPatronymic: TextInputEditText
    private lateinit var tilBirthDate: TextInputLayout
    private lateinit var etBirthDate: TextInputEditText
    private lateinit var rgGender: RadioGroup
    private lateinit var btnNext: Button

    // данные из первого шага регистрации
    private var emailArg: String = ""
    private var passwordArg: String = ""

    // форматирование даты
    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_DriveNext)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_step2)

        emailArg = intent.getStringExtra("extra_email").orEmpty()
        passwordArg = intent.getStringExtra("extra_password").orEmpty()

        initViews()
        setupDatePicker()
        setupValidationWatchers()
        setupListeners()
    }

    private fun initViews() {
        btnBack = findViewById(R.id.btnBack)
        tilLastName = findViewById(R.id.tilLastName)
        etLastName = findViewById(R.id.etLastName)
        tilFirstName = findViewById(R.id.tilFirstName)
        etFirstName = findViewById(R.id.etFirstName)
        tilPatronymic = findViewById(R.id.tilPatronymic)
        etPatronymic = findViewById(R.id.etPatronymic)
        tilBirthDate = findViewById(R.id.tilBirthDate)
        etBirthDate = findViewById(R.id.etBirthDate)
        rgGender = findViewById(R.id.rgGender)
        btnNext = findViewById(R.id.btnNext)
    }

    private fun setupDatePicker() {
        fun showPicker() {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Выбор даты рождения")
                .build()

            datePicker.addOnPositiveButtonClickListener { selectedUtcMillis ->
                val formattedDate = dateFormatter.format(Date(selectedUtcMillis))
                etBirthDate.setText(formattedDate)
                tilBirthDate.error = null
                updateNextButtonState()
            }

            datePicker.show(supportFragmentManager, "birth_date_picker")
        }

        etBirthDate.setOnClickListener { showPicker() }
        tilBirthDate.setStartIconOnClickListener { showPicker() }
    }

    private fun setupValidationWatchers() {
        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateNextButtonState()
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        etLastName.addTextChangedListener(watcher)
        etFirstName.addTextChangedListener(watcher)
        rgGender.setOnCheckedChangeListener { _, _ -> updateNextButtonState() }
    }

    private fun updateNextButtonState() {
        val isLastNameFilled = etLastName.text.toString().trim().isNotEmpty()
        val isFirstNameFilled = etFirstName.text.toString().trim().isNotEmpty()
        val isBirthDateFilled = etBirthDate.text.toString().trim().isNotEmpty()
        val isGenderSelected = rgGender.checkedRadioButtonId != -1

        // кнопка далее активна только после заполнения обязательных полей
        btnNext.isEnabled = isLastNameFilled && isFirstNameFilled && isBirthDateFilled && isGenderSelected
    }

    private fun setupListeners() {
        // кнопка назад возвращает на шаг 1
        btnBack.setOnClickListener {
            finish()
        }

        // кнопка далее
        btnNext.setOnClickListener {
            val lastName = etLastName.text.toString().trim()
            val firstName = etFirstName.text.toString().trim()
            val patronymic = etPatronymic.text.toString().trim()
            val birthDate = etBirthDate.text.toString().trim()
            val selectedGenderId = rgGender.checkedRadioButtonId

            // проверка обязательных полей
            if (lastName.isEmpty() || firstName.isEmpty() || birthDate.isEmpty() || selectedGenderId == -1) {
                Toast.makeText(this, getString(R.string.error_fill_required_fields), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val gender = if (selectedGenderId == R.id.rbMale) {
                getString(R.string.gender_male)
            } else {
                getString(R.string.gender_female)
            }

            Toast.makeText(this, "Шаг 2 пройден", Toast.LENGTH_SHORT).show()

        }
    }
}