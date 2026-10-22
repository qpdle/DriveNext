package ru.mtuci.drivenext

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import ru.mtuci.drivenext.data.local.AppDatabase

class MyBookingsActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var rvBookingsList: RecyclerView
    private lateinit var tvEmptyBookings: TextView
    private lateinit var bookingAdapter: BookingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_DriveNext)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_bookings)

        btnBack = findViewById(R.id.btnBack)
        rvBookingsList = findViewById(R.id.rvBookingsList)
        tvEmptyBookings = findViewById(R.id.tvEmptyBookings)

        btnBack.setOnClickListener { finish() }

        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        loadBookingsFromDb()
    }

    private fun setupRecyclerView() {
        bookingAdapter = BookingAdapter(emptyList()) { booking ->
            val intent = Intent(this, BookingDetailsActivity::class.java).apply {
                putExtra("extra_booking_number", booking.bookingNumber)
            }
            startActivity(intent)
        }
        rvBookingsList.layoutManager = LinearLayoutManager(this)
        rvBookingsList.adapter = bookingAdapter
    }

    private fun loadBookingsFromDb() {
        val bookingDao = AppDatabase.getInstance(this).bookingDao()
        lifecycleScope.launch {
            val list = bookingDao.getAllBookings()
            if (list.isEmpty()) {
                tvEmptyBookings.visibility = View.VISIBLE
                rvBookingsList.visibility = View.GONE
            } else {
                tvEmptyBookings.visibility = View.GONE
                rvBookingsList.visibility = View.VISIBLE
                bookingAdapter.updateData(list)
            }
        }
    }
}