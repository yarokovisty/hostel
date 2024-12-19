package ru.tpu.web.database.booking

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction


object BookingModel : Table("bookings") {
    private val bookingId = BookingModel.integer("booking_id")
    private val roomId = BookingModel.integer("room_id")
    private val customerName = BookingModel.varchar("customer_name", 30)
    private val phone = BookingModel.varchar("phone", 30)
    private val startDay = BookingModel.varchar("start_day", 30)
    private val endDay = BookingModel.varchar("end_day", 30)
    private val price = BookingModel.integer("price")
    private val childBed = BookingModel.bool("child_bed")

    fun fetchAllBookings(): List<BookingDTO> = transaction {
        return@transaction BookingModel.selectAll().map(::bookingRowToBookingDTO)
    }

    fun fetchParamsBookings(listRoomId: List<Int>): List<BookingDTO> = transaction {
        return@transaction BookingModel.selectAll().where { roomId inList listRoomId }.map(::bookingRowToBookingDTO)
    }

    fun bookingRoom(bookingDTO: BookingDTO) = transaction {
        BookingModel.insert{
            it[bookingId] = bookingDTO.bookingId
            it[roomId] = bookingDTO.roomId
            it[customerName] = bookingDTO.customerName
            it[phone] = bookingDTO.phone
            it[startDay] = bookingDTO.startDay
            it[endDay] = bookingDTO.endDay
            it[price] = bookingDTO.price
            it[childBed] = bookingDTO.childBed
        }
    }

    fun freeRoom(phone: String): Boolean = transaction {
        val countRoom = BookingModel.deleteWhere { BookingModel.phone eq phone }
        return@transaction countRoom == 1
    }

    private fun bookingRowToBookingDTO(bookingRow: ResultRow): BookingDTO =
        BookingDTO(
            bookingId = bookingRow[bookingId],
            roomId = bookingRow[roomId],
            customerName = bookingRow[customerName],
            phone = bookingRow[phone],
            startDay = bookingRow[startDay],
            endDay = bookingRow[endDay],
            price = bookingRow[price],
            childBed = bookingRow[childBed]
        )
}