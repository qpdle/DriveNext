package ru.mtuci.drivenext

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.mtuci.drivenext.data.local.AppDatabase
import ru.mtuci.drivenext.data.local.entity.FavoriteCarEntity

class CarDetailsActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var btnFavorite: ImageView
    private lateinit var ivCarImage: ImageView
    private lateinit var tvCarName: TextView
    private lateinit var tvCarAddress: TextView
    private lateinit var tvCarDescription: TextView
    private lateinit var tvPriceBottom: TextView
    private lateinit var btnBook: Button

    private lateinit var currentCar: CarItem
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_DriveNext)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_details)

        readIntentData()
        initViews()
        bindCarData()
        checkFavoriteStatus()
        setupListeners()
    }

    private fun readIntentData() {
        @Suppress("DEPRECATION")
        val car = intent.getSerializableExtra("extra_car") as? CarItem
        currentCar = car ?: CarItem(
            id = "1",
            brand = "Tesla",
            model = "Model 3 2019",
            pricePerDay = 2500,
            transmission = "Редуктор",
            fuelType = "Электро",
            imageResId = R.drawable.ic_splash_logo,
            address = "Авиамоторная ул., 8, стр. 2",
            description = "Tesla Model 3 оснащена мощным электродвигателем, автопилотом, премиальной аудиосистемой и панорамной крышей."
        )
    }

    private fun initViews() {
        btnBack = findViewById(R.id.btnBack)
        btnFavorite = findViewById(R.id.btnFavorite)
        ivCarImage = findViewById(R.id.ivCarImage)
        tvCarName = findViewById(R.id.tvCarName)
        tvCarAddress = findViewById(R.id.tvCarAddress)
        tvCarDescription = findViewById(R.id.tvCarDescription)
        tvPriceBottom = findViewById(R.id.tvPriceBottom)
        btnBook = findViewById(R.id.btnBook)
    }

    private fun bindCarData() {
        tvCarName.text = "${currentCar.brand} ${currentCar.model}"
        tvCarAddress.text = currentCar.address
        tvCarDescription.text = currentCar.description
        tvPriceBottom.text = getString(R.string.price_per_day_format, currentCar.pricePerDay.toString())
        ivCarImage.setImageResource(currentCar.imageResId)
    }

    private fun checkFavoriteStatus() {
        val favoriteDao = AppDatabase.getInstance(this).favoriteCarDao()
        lifecycleScope.launch {
            isFavorite = favoriteDao.isFavorite(currentCar.id)
            updateFavoriteIcon(isFavorite)
        }
    }

    private fun updateFavoriteIcon(favorite: Boolean) {
        if (favorite) {
            btnFavorite.setImageResource(R.drawable.ic_heart_filled)
        } else {
            btnFavorite.setImageResource(R.drawable.ic_heart_outline)
        }
    }

    private fun setupListeners() {
        btnBack.setOnClickListener {
            finish()
        }

        btnFavorite.setOnClickListener {
            val favoriteDao = AppDatabase.getInstance(this).favoriteCarDao()
            lifecycleScope.launch {
                if (isFavorite) {
                    favoriteDao.deleteFavoriteById(currentCar.id)
                    isFavorite = false
                    updateFavoriteIcon(false)
                    Toast.makeText(this@CarDetailsActivity, getString(R.string.msg_removed_from_favorites), Toast.LENGTH_SHORT).show()
                } else {
                    val entity = FavoriteCarEntity(
                        id = currentCar.id,
                        brand = currentCar.brand,
                        model = currentCar.model,
                        pricePerDay = currentCar.pricePerDay,
                        transmission = currentCar.transmission,
                        fuelType = currentCar.fuelType,
                        imageResId = currentCar.imageResId,
                        address = currentCar.address,
                        description = currentCar.description
                    )
                    favoriteDao.insertFavorite(entity)
                    isFavorite = true
                    updateFavoriteIcon(true)
                    Toast.makeText(this@CarDetailsActivity, getString(R.string.msg_added_to_favorites), Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnBook.setOnClickListener {
            Toast.makeText(this, "Переход к бронированию: ${currentCar.brand} ${currentCar.model}", Toast.LENGTH_SHORT).show()
        }
    }
}