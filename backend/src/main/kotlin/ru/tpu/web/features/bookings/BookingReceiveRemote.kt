package ru.tpu.web.features.bookings

import kotlinx.serialization.Serializable

@Serializable
data class BookingReceiveRemote(
    val capacity: String,
    val type: String,
    val customerName: String,
    val phone: String,
    val childBed: Boolean,
    val startDay: String,
    val endDay: String
)