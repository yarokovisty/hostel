package ru.tpu.web.database.rooms

data class RoomDTO(
    val roomId: Int,
    val type: String,
    val capacity: Int,
    val weekdayPrice: Int,
    val weekendPrice: Int
)