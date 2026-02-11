package org

import io.ktor.server.application.*
import io.ktor.server.netty.EngineMain
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import org.jetbrains.exposed.sql.Database
import io.ktor.serialization.kotlinx.json.*

fun configDb() {
    Database.connect("jdbc:sqlite:db.sqlite", driver = "org.sqlite.JDBC")
}

fun main(args: Array<String>) {
    configDb()
    EngineMain.main(args)
}

fun Application.module() {
    configureData()
    configureSerialization()
    configureRouting()
}

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
}
