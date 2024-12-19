package ru.tpu.web.features.bookings

import kotlinx.serialization.Serializable

@Serializable
data class BookingResponseRemote(
    val bookingId: Int,
    val roomId: Int,
    val customerName: String,
    val phone: String,
    val startDay: String,
    val endDay: String,
    val price: Int,
    val childBed: Boolean
)