package ru.tpu.web.features.bookings

import kotlinx.serialization.Serializable

@Serializable
data class FreeRoomReceiveRemote(
    val phone: String
)