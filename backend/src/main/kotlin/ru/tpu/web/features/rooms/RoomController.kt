package ru.tpu.web.features.rooms

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.tpu.web.database.rooms.RoomDTO
import ru.tpu.web.database.rooms.RoomModel

class RoomController(private val call: ApplicationCall) {

    suspend fun fetchAllRooms(){
        try {
            val rooms = RoomModel.fetchAllRooms().map(::roomDTOToRoomResponseRemote)
            call.respond(HttpStatusCode.OK, rooms)
        } catch (ex: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "Не удалось вытащить номера")
        }
    }

    suspend fun fetchParamsRooms() {
        try {
            val receive = call.receive(RoomReceiveRemote::class)
            val rooms = RoomModel.fetchParamsRooms(receive.capacity, receive.type).map(::roomDTOToRoomResponseRemote)
            call.respond(HttpStatusCode.OK, rooms)
        } catch (ex: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "Не удалось вытащить номера")
        }
    }

    private fun roomDTOToRoomResponseRemote(roomDTO: RoomDTO): RoomResponseRemote =
        RoomResponseRemote(
            roomId = roomDTO.roomId,
            type = roomDTO.type,
            capacity = roomDTO.capacity,
            weekendPrice = roomDTO.weekendPrice,
            weekdayPrice = roomDTO.weekdayPrice
        )
}