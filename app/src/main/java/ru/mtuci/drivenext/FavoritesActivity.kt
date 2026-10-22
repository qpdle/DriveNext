package ru.mtuci.drivenext

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import ru.mtuci.drivenext.data.local.AppDatabase

class FavoritesActivity : AppCompatActivity() {

    private lateinit var rvFavoritesList: RecyclerView
    private lateinit var tvEmptyFavorites: TextView
    private lateinit var carAdapter: CarAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_DriveNext)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        rvFavoritesList = findViewById(R.id.rvFavoritesList)
        tvEmptyFavorites = findViewById(R.id.tvEmptyFavorites)

        setupRecyclerView()
        setupBottomNavigation()
    }

    override fun onResume() {
        super.onResume()
        loadFavoritesFromDb()
    }

    private fun setupRecyclerView() {
        carAdapter = CarAdapter(
            cars = emptyList(),
            onBookClick = { car ->
                val intent = Intent(this, BookingCheckoutActivity::class.java).apply {
                    putExtra("extra_car", car)
                }
                startActivity(intent)
            },
            onDetailsClick = { car ->
                val intent = Intent(this, CarDetailsActivity::class.java).apply {
                    putExtra("extra_car", car)
                }
                startActivity(intent)
            }
        )

        rvFavoritesList.layoutManager = LinearLayoutManager(this)
        rvFavoritesList.adapter = carAdapter
    }

    private fun loadFavoritesFromDb() {
        val favoriteDao = AppDatabase.getInstance(this).favoriteCarDao()
        lifecycleScope.launch {
            val entities = favoriteDao.getAllFavorites()
            if (entities.isEmpty()) {
                tvEmptyFavorites.visibility = View.VISIBLE
                rvFavoritesList.visibility = View.GONE
            } else {
                tvEmptyFavorites.visibility = View.GONE
                rvFavoritesList.visibility = View.VISIBLE

                val cars = entities.map { entity ->
                    CarItem(
                        id = entity.id,
                        brand = entity.brand,
                        model = entity.model,
                        pricePerDay = entity.pricePerDay,
                        transmission = entity.transmission,
                        fuelType = entity.fuelType,
                        imageResId = entity.imageResId,
                        address = entity.address,
                        description = entity.description
                    )
                }
                carAdapter.updateData(cars)
            }
        }
    }

    private fun setupBottomNavigation() {
        val tabHome: LinearLayout = findViewById(R.id.tabHome)
        val tabSettings: LinearLayout = findViewById(R.id.tabSettings)

        tabHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            startActivity(intent)
            finish()
        }

        tabSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            startActivity(intent)
            finish()
        }
    }
}