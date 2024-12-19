package ru.tpu.web.database.rooms

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object RoomModel : Table("rooms") {
    private val roomId = RoomModel.integer("room_id")
    private val type = RoomModel.varchar("type", 30)
    private val capacity = RoomModel.integer("capacity")
    private val weekdayPrice = RoomModel.integer("weekday_price")
    private val weekendPrice = RoomModel.integer("weekend_price")

    fun fetchAllRooms(): List<RoomDTO> = transaction {
        return@transaction RoomModel.selectAll().map(::roomRowToRoomDTO)
    }

    fun fetchParamsRooms(capacity: Int, type: String): List<RoomDTO> = transaction {
        return@transaction RoomModel.selectAll().where { (RoomModel.capacity eq capacity) and  (RoomModel.type eq type) }.map(::roomRowToRoomDTO)
    }

    private fun roomRowToRoomDTO(roomRow: ResultRow): RoomDTO =
        RoomDTO(
            roomId = roomRow[roomId],
            type = roomRow[type],
            capacity = roomRow[capacity],
            weekdayPrice = roomRow[weekdayPrice],
            weekendPrice = roomRow[weekendPrice]
        )
}