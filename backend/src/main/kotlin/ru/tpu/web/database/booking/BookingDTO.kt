package ru.tpu.web.database.booking

import java.util.Date

data class BookingDTO(
    val bookingId: Int,
    val roomId: Int,
    val customerName: String,
    val phone: String,
    val startDay: String,
    val endDay: String,
    val price: Int,
    val childBed: Boolean
)
