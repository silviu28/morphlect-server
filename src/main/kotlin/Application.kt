package org

import io.ktor.server.application.*
import io.ktor.server.netty.EngineMain
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import org.jetbrains.exposed.sql.Database
import io.ktor.serialization.kotlinx.json.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.schema.Models

fun configDb() {
    Database.connect("jdbc:sqlite:db.sqlite", driver = "org.sqlite.JDBC")
    transaction {
        SchemaUtils.createMissingTablesAndColumns()
    }
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
