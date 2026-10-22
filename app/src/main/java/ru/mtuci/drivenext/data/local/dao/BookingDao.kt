package ru.mtuci.drivenext.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ru.mtuci.drivenext.data.local.entity.BookingEntity

@Dao
interface BookingDao {

    @Query("SELECT * FROM bookings ORDER BY bookingNumber DESC")
    suspend fun getAllBookings(): List<BookingEntity>

    @Query("SELECT * FROM bookings WHERE bookingNumber = :number LIMIT 1")
    suspend fun getBookingByNumber(number: String): BookingEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: BookingEntity)

    @Query("UPDATE bookings SET status = :newStatus WHERE bookingNumber = :number")
    suspend fun updateBookingStatus(number: String, newStatus: String)

    @Query("DELETE FROM bookings WHERE bookingNumber = :number")
    suspend fun deleteBooking(number: String)
}