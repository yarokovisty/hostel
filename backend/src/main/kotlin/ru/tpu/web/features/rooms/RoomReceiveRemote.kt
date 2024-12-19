package ru.tpu.web.features.rooms

import kotlinx.serialization.Serializable

@Serializable
data class RoomReceiveRemote(
    val capacity: Int,
    val type: String
)
