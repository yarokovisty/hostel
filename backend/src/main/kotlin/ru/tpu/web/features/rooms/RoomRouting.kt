package ru.tpu.web.features.rooms

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRoom() {
    routing {
        get("/rooms") {
            val roomController = RoomController(call)
            roomController.fetchAllRooms()
        }

        post("/room") {
            val roomController = RoomController(call)
            roomController.fetchParamsRooms()
        }
    }
}