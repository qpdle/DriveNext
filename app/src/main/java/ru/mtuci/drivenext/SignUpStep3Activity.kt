package ru.mtuci.drivenext

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SignUpStep3Activity : AppCompatActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var ivAvatar: ImageView
    private lateinit var tilDriverLicense: TextInputLayout
    private lateinit var etDriverLicense: TextInputEditText
    private lateinit var tilIssueDate: TextInputLayout
    private lateinit var etIssueDate: TextInputEditText
    private lateinit var btnUploadLicense: AppCompatButton
    private lateinit var btnUploadPassport: AppCompatButton
    private lateinit var btnNext: Button

    // данные с предыдущих экранов
    private var emailArg: String = ""
    private var passwordArg: String = ""
    private var lastNameArg: String = ""
    private var firstNameArg: String = ""
    private var patronymicArg: String = ""
    private var birthDateArg: String = ""
    private var genderArg: String = ""

    // хранение Uri выбранных фотографий
    private var avatarUri: Uri? = null
    private var licensePhotoUri: Uri? = null
    private var passportPhotoUri: Uri? = null

    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    // контракт для выбора фото аватара
    private val pickAvatarLauncher = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            avatarUri = uri
            ivAvatar.setImageURI(uri)
        }
    }

    // контракт для выбора фото водительского удостоверения
    private val pickLicenseLauncher = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            licensePhotoUri = uri
            btnUploadLicense.text = getString(R.string.photo_uploaded)
            updateNextButtonState()
        }
    }

    // контракт для выбора фото паспорта
    private val pickPassportLauncher = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            passportPhotoUri = uri
            btnUploadPassport.text = getString(R.string.photo_uploaded)
            updateNextButtonState()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_DriveNext)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_step3)

        readIntentExtras()
        initViews()
        setupDatePicker()
        setupImagePickers()
        setupTextWatchers()
        setupListeners()
    }

    private fun readIntentExtras() {
        emailArg = intent.getStringExtra("extra_email").orEmpty()
        passwordArg = intent.getStringExtra("extra_password").orEmpty()
        lastNameArg = intent.getStringExtra("extra_last_name").orEmpty()
        firstNameArg = intent.getStringExtra("extra_first_name").orEmpty()
        patronymicArg = intent.getStringExtra("extra_patronymic").orEmpty()
        birthDateArg = intent.getStringExtra("extra_birth_date").orEmpty()
        genderArg = intent.getStringExtra("extra_gender").orEmpty()
    }

    private fun initViews() {
        btnBack = findViewById(R.id.btnBack)
        ivAvatar = findViewById(R.id.ivAvatar)
        tilDriverLicense = findViewById(R.id.tilDriverLicense)
        etDriverLicense = findViewById(R.id.etDriverLicense)
        tilIssueDate = findViewById(R.id.tilIssueDate)
        etIssueDate = findViewById(R.id.etIssueDate)
        btnUploadLicense = findViewById(R.id.btnUploadLicense)
        btnUploadPassport = findViewById(R.id.btnUploadPassport)
        btnNext = findViewById(R.id.btnNext)
    }

    private fun setupDatePicker() {
        fun showPicker() {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Выбор даты выдачи")
                .build()

            datePicker.addOnPositiveButtonClickListener { selectedUtcMillis ->
                val formattedDate = dateFormatter.format(Date(selectedUtcMillis))
                etIssueDate.setText(formattedDate)
                tilIssueDate.error = null
                updateNextButtonState()
            }

            datePicker.show(supportFragmentManager, "issue_date_picker")
        }

        etIssueDate.setOnClickListener { showPicker() }
        tilIssueDate.setStartIconOnClickListener { showPicker() }
    }

    private fun setupImagePickers() {
        // выбор фото аватара
        ivAvatar.setOnClickListener {
            pickAvatarLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }

        // выбор фото прав
        btnUploadLicense.setOnClickListener {
            pickLicenseLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }

        // выбор фото паспорта
        btnUploadPassport.setOnClickListener {
            pickPassportLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
    }

    private fun setupTextWatchers() {
        etDriverLicense.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tilDriverLicense.error = null
                updateNextButtonState()
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun updateNextButtonState() {
        val license = etDriverLicense.text.toString().trim()
        val issueDate = etIssueDate.text.toString().trim()

        // 10 символов номера ВУ, заполненная дата и оба загруженных фото
        val isLicenseValid = license.length == 10
        val isDateFilled = issueDate.isNotEmpty()
        val arePhotosUploaded = (licensePhotoUri != null) && (passportPhotoUri != null)

        btnNext.isEnabled = isLicenseValid && isDateFilled && arePhotosUploaded
    }

    private fun setupListeners() {
        // назад: возврат на шаг 2
        btnBack.setOnClickListener {
            finish()
        }

        // далее: переход на экран поздравления
        btnNext.setOnClickListener {
            val license = etDriverLicense.text.toString().trim()
            val issueDate = etIssueDate.text.toString().trim()

            if (license.length != 10) {
                tilDriverLicense.error = getString(R.string.error_license_length)
                return@setOnClickListener
            }

            if (issueDate.isEmpty()) {
                tilIssueDate.error = getString(R.string.error_invalid_issue_date)
                return@setOnClickListener
            }

            if (licensePhotoUri == null || passportPhotoUri == null) {
                Toast.makeText(this, getString(R.string.error_missing_photos), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(this, "Шаг 3 пройден! Все документы загружены", Toast.LENGTH_SHORT).show()
            // переход на экран поздравления
            val intent = Intent(this, SignUpSuccessActivity::class.java)
            startActivity(intent)
        }
    }
}