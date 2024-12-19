package ru.tpu.web.features.bookings

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.tpu.web.database.booking.BookingDTO
import ru.tpu.web.database.booking.BookingModel
import ru.tpu.web.database.rooms.RoomDTO
import ru.tpu.web.database.rooms.RoomModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.random.Random

class BookingController(private val call: ApplicationCall) {

    suspend fun fetchAllBookings() {
        runCatching {
            BookingModel.fetchAllBookings()
        }.onSuccess { bookings ->
            call.respond(HttpStatusCode.OK, bookings)
        }.onFailure { ex ->
            call.respond(HttpStatusCode.InternalServerError, "Ошибка в получении всех броней: ${ex.message}")
        }
    }

    suspend fun bookRoom() {
        runCatching {
            val bookingReceiveRemote = call.receive(BookingReceiveRemote::class)
            val availableRooms = findAvailableRoom(bookingReceiveRemote)
            createBooking(availableRooms, bookingReceiveRemote)
        }.onFailure { ex ->
            call.respond(HttpStatusCode.InternalServerError, "Ошибка в бронировании номера: ${ex.message}")
        }
    }

    suspend fun freeRoom() {
        runCatching {
            val receive = call.receive(FreeRoomReceiveRemote::class)
            BookingModel.freeRoom(receive.phone)
        }.onSuccess {
            call.respond(HttpStatusCode.NoContent)
        }.onFailure { ex ->
            call.respond(HttpStatusCode.InternalServerError, "Не удалось освободить номер: ${ex.message}")
        }
    }

    private fun findAvailableRoom(bookingReceiveRemote: BookingReceiveRemote): RoomDTO? {
        val rooms = RoomModel.fetchParamsRooms(bookingReceiveRemote.capacity.toInt(), bookingReceiveRemote.type)
        val bookings = BookingModel.fetchParamsBookings(rooms.map { it.roomId })
        val bookedRoomIds = checkFreeRooms(bookingReceiveRemote.startDay, bookingReceiveRemote.endDay, bookings)
        return rooms.firstOrNull { it.roomId !in bookedRoomIds }
    }

    private suspend fun createBooking(room: RoomDTO?, bookingReceiveRemote: BookingReceiveRemote) {
        if (room == null) {
            call.respond(HttpStatusCode.NotFound, "Нет свободных номеров на этот период")
            return
        }

        val bookingId = Random.nextInt(0, 1_000_000)
        val price = calculatePrice(bookingReceiveRemote.startDay, bookingReceiveRemote.endDay, room)

        val bookingDTO = BookingDTO(
            bookingId = bookingId,
            roomId = room.roomId,
            customerName = bookingReceiveRemote.customerName,
            phone = bookingReceiveRemote.phone,
            startDay = bookingReceiveRemote.startDay,
            endDay = bookingReceiveRemote.endDay,
            price = price,
            childBed = bookingReceiveRemote.childBed
        )

        BookingModel.bookingRoom(bookingDTO)
        call.respond(HttpStatusCode.Created)
    }

    private fun calculatePrice(startDay: String, endDay: String, roomDTO: RoomDTO): Int {
        val formatter = DateTimeFormatter.ofPattern(DATE_FORMAT)
        val startDate = LocalDate.parse(startDay, formatter)
        val endDate = LocalDate.parse(endDay, formatter)

        val totalPrice = (startDate..endDate).sumOf { date ->
            if (isWeekend(date)) roomDTO.weekendPrice else roomDTO.weekdayPrice
        }

        return if (totalPrice > SALE_DAYS) (totalPrice * DISCOUNT_FACTOR).toInt() else totalPrice
    }

    private fun checkFreeRooms(startDay: String, endDay: String, bookings: List<BookingDTO>): Set<Int> {
        val formatter = DateTimeFormatter.ofPattern(DATE_FORMAT)
        val startDate = LocalDate.parse(startDay, formatter)
        val endDate = LocalDate.parse(endDay, formatter)

        return bookings.filter { booking ->
            val bookingStartDate = LocalDate.parse(booking.startDay, formatter)
            val bookingEndDate = LocalDate.parse(booking.endDay, formatter)

            isDateOverlap(startDate, endDate, bookingStartDate, bookingEndDate)
        }.mapTo(mutableSetOf()) { it.roomId }
    }

    private fun isDateOverlap(startDate: LocalDate, endDate: LocalDate, bookingStart: LocalDate, bookingEnd: LocalDate): Boolean {
        return startDate <= bookingEnd && endDate >= bookingStart
    }

    private fun isWeekend(date: LocalDate): Boolean {
        return date.dayOfWeek in setOf(DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)
    }

    companion object {
        private const val DATE_FORMAT = "dd.MM.yyyy"
        private const val SALE_DAYS = 7
        private const val DISCOUNT_FACTOR = 0.9
    }
}

operator fun LocalDate.rangeTo(other: LocalDate): Sequence<LocalDate> =
    generateSequence(this) { if (it < other) it.plusDays(1) else null }
