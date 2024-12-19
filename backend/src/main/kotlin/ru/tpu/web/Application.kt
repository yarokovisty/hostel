package ru.tpu.web

import io.ktor.server.application.*
import ru.tpu.web.features.bookings.configureBooking
import ru.tpu.web.features.rooms.configureRoom
import ru.tpu.web.plugins.configureDatabases
import ru.tpu.web.plugins.configureRouting
import ru.tpu.web.plugins.configureSerialization

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureDatabases()
    configureRouting()
    configureRoom()
    configureBooking()
}
