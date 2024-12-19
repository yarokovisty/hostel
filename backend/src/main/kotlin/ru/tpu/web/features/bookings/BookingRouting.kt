package ru.tpu.web.features.bookings

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureBooking() {

    routing {
        get("/bookings") {
            val bookingController = BookingController(call)
            bookingController.fetchAllBookings()
        }

        post("/book") {
            val bookingController = BookingController(call)
            bookingController.bookRoom()
        }

        delete("/book") {
            val bookingController = BookingController(call)
            bookingController.freeRoom()
        }
    }
}