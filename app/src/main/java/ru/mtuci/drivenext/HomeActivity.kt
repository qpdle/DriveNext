package ru.mtuci.drivenext

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeActivity : AppCompatActivity() {

    private lateinit var searchViewHome: SearchView
    private lateinit var rvCarsList: RecyclerView
    private lateinit var tabHome: LinearLayout
    private lateinit var tabFavorites: LinearLayout
    private lateinit var tabSettings: LinearLayout
    private lateinit var carAdapter: CarAdapter

    // Тестовый каталог автомобилей согласно макетам и ТЗ
    private val availableCars = listOf(
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
        setContentView(R.layout.activity_home)

        initViews()
        setupRecyclerView()
        setupSearch()
        setupBottomNavigation()
    }

    private fun initViews() {
        searchViewHome = findViewById(R.id.searchViewHome)
        rvCarsList = findViewById(R.id.rvCarsList)
        tabHome = findViewById(R.id.tabHome)
        tabFavorites = findViewById(R.id.tabFavorites)
        tabSettings = findViewById(R.id.tabSettings)
    }

    private fun setupRecyclerView() {
        carAdapter = CarAdapter(
            cars = availableCars,
            onBookClick = { car ->
                Toast.makeText(this, "Бронирование: ${car.brand} ${car.model}", Toast.LENGTH_SHORT).show()
            },
            onDetailsClick = { car ->
                Toast.makeText(this, "Детали: ${car.brand} ${car.model}", Toast.LENGTH_SHORT).show()
            }
        )
        rvCarsList.layoutManager = LinearLayoutManager(this)
        rvCarsList.adapter = carAdapter
    }

    private fun setupSearch() {
        searchViewHome.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val trimmedQuery = query.orEmpty().trim()
                if (trimmedQuery.isNotEmpty()) {
                    val intent = Intent(this@HomeActivity, SearchResultsActivity::class.java).apply {
                        putExtra("extra_search_query", trimmedQuery)
                    }
                    startActivity(intent)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun setupBottomNavigation() {
        tabHome.setOnClickListener {
            // Уже на главном экране
        }

        tabFavorites.setOnClickListener {
            Toast.makeText(this, "Раздел «Избранное» будет доступен в следующем обновлении", Toast.LENGTH_SHORT).show()
        }

        tabSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}