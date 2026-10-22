package ru.mtuci.drivenext

import ru.mtuci.drivenext.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.mtuci.drivenext.data.local.entity.BookingEntity

class BookingAdapter(
    private var bookings: List<BookingEntity>,
    private val onBookingClick: (BookingEntity) -> Unit
) : RecyclerView.Adapter<BookingAdapter.BookingViewHolder>() {

    inner class BookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvBookingCarTitle: TextView = itemView.findViewById(R.id.tvBookingCarTitle)
        val tvBookingDateInfo: TextView = itemView.findViewById(R.id.tvBookingDateInfo)

        fun bind(booking: BookingEntity) {
            tvBookingCarTitle.text = "${booking.carBrand} ${booking.carModel}"

            if (booking.status == "Одобрено") {
                tvBookingDateInfo.text = itemView.context.getString(R.string.booking_active_prefix, booking.startDate)
            } else {
                tvBookingDateInfo.text = itemView.context.getString(R.string.booking_completed_prefix, booking.endDate)
            }

            itemView.setOnClickListener { onBookingClick(booking) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_booking_card, parent, false)
        return BookingViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        holder.bind(bookings[position])
    }

    override fun getItemCount(): Int = bookings.size

    fun updateData(newBookings: List<BookingEntity>) {
        bookings = newBookings
        notifyDataSetChanged()
    }
}