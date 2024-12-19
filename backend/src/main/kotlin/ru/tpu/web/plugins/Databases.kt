package ru.tpu.web.plugins

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.sql.Connection
import java.sql.DriverManager
import org.jetbrains.exposed.sql.*

fun Application.configureDatabases() {
    Database.connect(
        url = "jdbc:postgresql://localhost:5432/hostel",
        user = "postgres",
        password = "zhjckfdcsy2003"
    )
}
