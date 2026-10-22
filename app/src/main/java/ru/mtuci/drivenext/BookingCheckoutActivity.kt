package ru.mtuci.drivenext

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.util.Pair
import androidx.lifecycle.lifecycleScope
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.launch
import ru.mtuci.drivenext.data.local.AppDatabase
import ru.mtuci.drivenext.data.local.entity.BookingEntity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class BookingCheckoutActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var tvCarName: TextView
    private lateinit var tvCarPricePerDay: TextView
    private lateinit var ivCarImage: ImageView
    private lateinit var layoutDatePickers: ConstraintLayout
    private lateinit var tvStartDate: TextView
    private lateinit var tvEndDate: TextView
    private lateinit var tvRentDaysTitle: TextView
    private lateinit var tvRentDaysTotal: TextView
    private lateinit var tvInsuranceTitle: TextView
    private lateinit var tvInsuranceTotal: TextView
    private lateinit var tvTotalPrice: TextView
    private lateinit var tvDepositPrice: TextView
    private lateinit var btnContinueBooking: Button

    private lateinit var currentCar: CarItem
    private var daysCount = 3
    private val insurancePerDay = 300
    private val depositAmount = 15000

    private val dateFormatter = SimpleDateFormat("dd MMMM yyyy", Locale("ru"))
    private var startCalendar: Calendar = Calendar.getInstance()
    private var endCalendar: Calendar = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 3) }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_DriveNext)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_checkout)

        readIntentData()
        initViews()
        setupCarData()
        updateDatesDisplay()
        recalculatePrices()
        setupListeners()
    }

    private fun readIntentData() {
        @Suppress("DEPRECATION")
        val car = intent.getSerializableExtra("extra_car") as? CarItem
        currentCar = car ?: CarItem(
            id = "1",
            brand = "Mercedes-Benz",
            model = "S 500 Sedan",
            pricePerDay = 2500,
            transmission = "А/Т",
            fuelType = "Бензин",
            imageResId = R.drawable.ic_splash_logo,
            address = "Авиамоторная ул., 8, стр. 2"
        )
    }

    private fun initViews() {
        btnBack = findViewById(R.id.btnBack)
        tvCarName = findViewById(R.id.tvCarName)
        tvCarPricePerDay = findViewById(R.id.tvCarPricePerDay)
        ivCarImage = findViewById(R.id.ivCarImage)
        layoutDatePickers = findViewById(R.id.layoutDatePickers)
        tvStartDate = findViewById(R.id.tvStartDate)
        tvEndDate = findViewById(R.id.tvEndDate)
        tvRentDaysTitle = findViewById(R.id.tvRentDaysTitle)
        tvRentDaysTotal = findViewById(R.id.tvRentDaysTotal)
        tvInsuranceTitle = findViewById(R.id.tvInsuranceTitle)
        tvInsuranceTotal = findViewById(R.id.tvInsuranceTotal)
        tvTotalPrice = findViewById(R.id.tvTotalPrice)
        tvDepositPrice = findViewById(R.id.tvDepositPrice)
        btnContinueBooking = findViewById(R.id.btnContinueBooking)
    }

    private fun setupCarData() {
        tvCarName.text = "${currentCar.brand} ${currentCar.model}"
        tvCarPricePerDay.text = getString(R.string.price_per_day_format, currentCar.pricePerDay.toString())
        ivCarImage.setImageResource(currentCar.imageResId)
        tvDepositPrice.text = getString(R.string.price_simple_rub_format, depositAmount.toString())
    }

    private fun updateDatesDisplay() {
        tvStartDate.text = "08:00, ${dateFormatter.format(startCalendar.time)}"
        tvEndDate.text = "08:00, ${dateFormatter.format(endCalendar.time)}"
    }

    private fun recalculatePrices() {
        val rentTotal = currentCar.pricePerDay * daysCount
        val insuranceTotal = insurancePerDay * daysCount
        val overallTotal = rentTotal + insuranceTotal

        tvRentDaysTitle.text = getString(R.string.checkout_rent_format, daysCount)
        tvRentDaysTotal.text = getString(R.string.price_simple_rub_format, rentTotal.toString())

        tvInsuranceTitle.text = getString(R.string.checkout_insurance_format, daysCount)
        tvInsuranceTotal.text = getString(R.string.price_simple_rub_format, insuranceTotal.toString())

        tvTotalPrice.text = getString(R.string.price_simple_rub_format, overallTotal.toString())
    }

    private fun setupListeners() {
        btnBack.setOnClickListener { finish() }

        layoutDatePickers.setOnClickListener {
            showDateRangePicker()
        }

        btnContinueBooking.setOnClickListener {
            saveBookingAndProceed()
        }
    }

    private fun showDateRangePicker() {
        val picker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Выберите период аренды")
            .setSelection(Pair(startCalendar.timeInMillis, endCalendar.timeInMillis))
            .build()

        picker.addOnPositiveButtonClickListener { selection ->
            val startMillis = selection.first
            val endMillis = selection.second
            if (startMillis != null && endMillis != null) {
                startCalendar.timeInMillis = startMillis
                endCalendar.timeInMillis = endMillis

                val diffMillis = endMillis - startMillis
                val computedDays = (diffMillis / (1000 * 60 * 60 * 24)).toInt()
                daysCount = if (computedDays <= 0) 1 else computedDays

                updateDatesDisplay()
                recalculatePrices()
            }
        }

        picker.show(supportFragmentManager, "booking_range_picker")
    }

    private fun saveBookingAndProceed() {
        val sharedPref = getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        val userEmail = sharedPref.getString("user_email", "ivanov@mtuci.ru").orEmpty()

        val generatedBookingNumber = (Random.nextInt(100000, 999999)).toString()
        val rentTotal = currentCar.pricePerDay * daysCount
        val insuranceTotal = insurancePerDay * daysCount
        val overallTotal = rentTotal + insuranceTotal

        val newBooking = BookingEntity(
            bookingNumber = generatedBookingNumber,
            carModel = currentCar.model,
            carBrand = currentCar.brand,
            pricePerDay = currentCar.pricePerDay,
            address = currentCar.address,
            startDate = tvStartDate.text.toString(),
            endDate = tvEndDate.text.toString(),
            driverName = "Иван Иванов",
            driverLicense = "4516463410",
            status = "Одобрено",
            daysCount = daysCount,
            rentPriceTotal = rentTotal,
            insuranceTotal = insuranceTotal,
            deposit = depositAmount,
            totalPrice = overallTotal,
            imageResId = currentCar.imageResId
        )

        val bookingDao = AppDatabase.getInstance(this).bookingDao()
        lifecycleScope.launch {
            bookingDao.insertBooking(newBooking)

            // Переход на экран успешного бронирования с очисткой промежуточных экранов
            val intent = Intent(this@BookingCheckoutActivity, BookingSuccessActivity::class.java).apply {
                putExtra("extra_booking_number", generatedBookingNumber)
            }
            startActivity(intent)
            finish()
        }
    }
}