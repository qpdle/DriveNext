package ru.mtuci.drivenext

import ru.mtuci.drivenext.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView

class CarAdapter(
    private var cars: List<CarItem>,
    private val onBookClick: (CarItem) -> Unit,
    private val onDetailsClick: (CarItem) -> Unit
) : RecyclerView.Adapter<CarAdapter.CarViewHolder>() {

    inner class CarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvModel: TextView = itemView.findViewById(R.id.tvCarModel)
        val tvBrand: TextView = itemView.findViewById(R.id.tvCarBrand)
        val tvPrice: TextView = itemView.findViewById(R.id.tvCarPrice)
        val tvTransmission: TextView = itemView.findViewById(R.id.tvCarTransmission)
        val tvFuel: TextView = itemView.findViewById(R.id.tvCarFuel)
        val ivImage: ImageView = itemView.findViewById(R.id.ivCarImage)
        val btnBook: Button = itemView.findViewById(R.id.btnBookCar)
        val btnDetails: AppCompatButton = itemView.findViewById(R.id.btnCarDetails)

        fun bind(car: CarItem) {
            tvModel.text = car.model
            tvBrand.text = car.brand
            tvPrice.text = itemView.context.getString(R.string.price_per_day_format, car.pricePerDay.toString())
            tvTransmission.text = car.transmission
            tvFuel.text = car.fuelType
            ivImage.setImageResource(car.imageResId)

            btnBook.setOnClickListener { onBookClick(car) }
            btnDetails.setOnClickListener { onDetailsClick(car) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_car_card, parent, false)
        return CarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        holder.bind(cars[position])
    }

    override fun getItemCount(): Int = cars.size

    fun updateData(newCars: List<CarItem>) {
        cars = newCars
        notifyDataSetChanged()
    }
}