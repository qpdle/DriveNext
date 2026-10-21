package ru.mtuci.drivenext

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchResultsActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var pbLoading: ProgressBar
    private lateinit var tvEmptyOrError: TextView
    private lateinit var rvResults: RecyclerView
    private lateinit var tabHome: LinearLayout
    private lateinit var tabFavorites: LinearLayout
    private lateinit var tabSettings: LinearLayout
    private lateinit var carAdapter: CarAdapter

    private val allCars = listOf(
        CarItem(
            id = "1",
            brand = "Mercedes-Benz",
            model = "S 500 Sedan",
            pricePerDay = 2500,
            transmission = "А/Т",
            fuelType = "Бензин",
            imageResId = R.drawable.ic_splash_logo
        ),
        CarItem(
            id = "2",
            brand = "BMW",
            model = "3 Series",
            pricePerDay = 2200,
            transmission = "А/Т",
            fuelType = "Бензин",
            imageResId = R.drawable.ic_splash_logo
        ),
        CarItem(
            id = "3",
            brand = "Audi",
            model = "A4",
            pricePerDay = 2100,
            transmission = "А/Т",
            fuelType = "Дизель",
            imageResId = R.drawable.ic_splash_logo
        ),
        CarItem(
            id = "4",
            brand = "Tesla",
            model = "Model 3 2019",
            pricePerDay = 2800,
            transmission = "Редуктор",
            fuelType = "Электро",
            imageResId = R.drawable.ic_splash_logo
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_DriveNext)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_results)

        initViews()
        setupRecyclerView()
        setupListeners()

        val query = intent.getStringExtra("extra_search_query").orEmpty()
        performSearch(query)
    }

    private fun initViews() {
        btnBack = findViewById(R.id.btnBackFromSearch)
        pbLoading = findViewById(R.id.pbSearchLoading)
        tvEmptyOrError = findViewById(R.id.tvEmptyOrError)
        rvResults = findViewById(R.id.rvSearchResults)
        tabHome = findViewById(R.id.tabHome)
        tabFavorites = findViewById(R.id.tabFavorites)
        tabSettings = findViewById(R.id.tabSettings)
    }

    private fun setupRecyclerView() {
        carAdapter = CarAdapter(
            cars = emptyList(),
            onBookClick = { car ->
                Toast.makeText(this, "Бронирование: ${car.brand} ${car.model}", Toast.LENGTH_SHORT).show()
            },
            onDetailsClick = { car ->
                val intent = Intent(this, CarDetailsActivity::class.java).apply {
                    putExtra("extra_car", car)
                }
                startActivity(intent)
            }
        )
        rvResults.layoutManager = LinearLayoutManager(this)
        rvResults.adapter = carAdapter
    }

    private fun setupListeners() {
        btnBack.setOnClickListener {
            finish()
        }

        tabHome.setOnClickListener {
            finish()
        }

        tabFavorites.setOnClickListener {
            Toast.makeText(this, "Раздел «Избранное» будет доступен в следующем обновлении", Toast.LENGTH_SHORT).show()
        }

        tabSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun performSearch(query: String) {
        pbLoading.visibility = View.VISIBLE
        rvResults.visibility = View.GONE
        tvEmptyOrError.visibility = View.GONE

        Handler(Looper.getMainLooper()).postDelayed({
            pbLoading.visibility = View.GONE

            val filteredList = allCars.filter { car ->
                car.brand.contains(query, ignoreCase = true) ||
                        car.model.contains(query, ignoreCase = true)
            }

            if (filteredList.isEmpty()) {
                tvEmptyOrError.text = getString(R.string.search_empty_message)
                tvEmptyOrError.visibility = View.VISIBLE
                rvResults.visibility = View.GONE
            } else {
                tvEmptyOrError.visibility = View.GONE
                rvResults.visibility = View.VISIBLE
                carAdapter.updateData(filteredList)
            }
        }, 300)
    }
}