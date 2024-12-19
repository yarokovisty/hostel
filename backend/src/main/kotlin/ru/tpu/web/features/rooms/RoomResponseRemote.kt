package ru.tpu.web.features.rooms

import kotlinx.serialization.Serializable

@Serializable
data class RoomResponseRemote(
    val roomId: Int,
    val type: String,
    val capacity: Int,
    val weekdayPrice: Int,
    val weekendPrice: Int
)