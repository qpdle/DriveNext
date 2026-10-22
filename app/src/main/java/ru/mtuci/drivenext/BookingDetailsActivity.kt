package ru.mtuci.drivenext

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.mtuci.drivenext.data.local.AppDatabase
import ru.mtuci.drivenext.data.local.entity.BookingEntity

class BookingDetailsActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var tvHeaderBookingNumber: TextView
    private lateinit var tvCarName: TextView
    private lateinit var tvCarPricePerDay: TextView
    private lateinit var ivCarImage: ImageView
    private lateinit var tvLocationAddress: TextView
    private lateinit var tvStartDate: TextView
    private lateinit var tvEndDate: TextView
    private lateinit var tvDriverName: TextView
    private lateinit var tvDriverLicense: TextView
    private lateinit var tvBookingStatus: TextView
    private lateinit var tvRentDaysTitle: TextView
    private lateinit var tvRentDaysTotal: TextView
    private lateinit var tvInsuranceTitle: TextView
    private lateinit var tvInsuranceTotal: TextView
    private lateinit var tvOverallSum: TextView
    private lateinit var btnCancelBooking: Button

    private var bookingNumber: String = ""
    private var currentBooking: BookingEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_DriveNext)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_details)

        bookingNumber = intent.getStringExtra("extra_booking_number").orEmpty()

        initViews()
        loadBookingDetails()
        setupListeners()
    }

    private fun initViews() {
        btnBack = findViewById(R.id.btnBack)
        tvHeaderBookingNumber = findViewById(R.id.tvHeaderBookingNumber)
        tvCarName = findViewById(R.id.tvCarName)
        tvCarPricePerDay = findViewById(R.id.tvCarPricePerDay)
        ivCarImage = findViewById(R.id.ivCarImage)
        tvLocationAddress = findViewById(R.id.tvLocationAddress)
        tvStartDate = findViewById(R.id.tvStartDate)
        tvEndDate = findViewById(R.id.tvEndDate)
        tvDriverName = findViewById(R.id.tvDriverName)
        tvDriverLicense = findViewById(R.id.tvDriverLicense)
        tvBookingStatus = findViewById(R.id.tvBookingStatus)
        tvRentDaysTitle = findViewById(R.id.tvRentDaysTitle)
        tvRentDaysTotal = findViewById(R.id.tvRentDaysTotal)
        tvInsuranceTitle = findViewById(R.id.tvInsuranceTitle)
        tvInsuranceTotal = findViewById(R.id.tvInsuranceTotal)
        tvOverallSum = findViewById(R.id.tvOverallSum)
        btnCancelBooking = findViewById(R.id.btnCancelBooking)
    }

    private fun loadBookingDetails() {
        val bookingDao = AppDatabase.getInstance(this).bookingDao()
        lifecycleScope.launch {
            val booking = bookingDao.getBookingByNumber(bookingNumber)
            if (booking != null) {
                currentBooking = booking
                bindData(booking)
            } else {
                finish()
            }
        }
    }

    private fun bindData(booking: BookingEntity) {
        tvHeaderBookingNumber.text = getString(R.string.booking_number_prefix_format, booking.bookingNumber)
        tvCarName.text = "${booking.carBrand} ${booking.carModel}"
        tvCarPricePerDay.text = getString(R.string.price_per_day_format, booking.pricePerDay.toString())
        ivCarImage.setImageResource(booking.imageResId)
        tvLocationAddress.text = booking.address
        tvStartDate.text = booking.startDate
        tvEndDate.text = booking.endDate
        tvDriverName.text = booking.driverName
        tvDriverLicense.text = booking.driverLicense
        tvBookingStatus.text = booking.status

        tvRentDaysTitle.text = getString(R.string.checkout_rent_format, booking.daysCount)
        tvRentDaysTotal.text = getString(R.string.price_simple_rub_format, booking.rentPriceTotal.toString())

        tvInsuranceTitle.text = getString(R.string.checkout_insurance_format, booking.daysCount)
        tvInsuranceTotal.text = getString(R.string.price_simple_rub_format, booking.insuranceTotal.toString())

        tvOverallSum.text = getString(R.string.price_simple_rub_format, booking.totalPrice.toString())
    }

    private fun setupListeners() {
        btnBack.setOnClickListener { finish() }

        btnCancelBooking.setOnClickListener {
            val bookingDao = AppDatabase.getInstance(this).bookingDao()
            lifecycleScope.launch {
                bookingDao.deleteBooking(bookingNumber)
                Toast.makeText(this@BookingDetailsActivity, getString(R.string.booking_canceled_toast), Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}