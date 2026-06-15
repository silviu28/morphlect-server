package org

import io.ktor.server.application.*
import io.ktor.server.netty.EngineMain
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import org.jetbrains.exposed.sql.Database
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.ratelimit.RateLimit
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.schema.Models
import kotlin.time.Duration.Companion.seconds

fun configDb() {
    val dbPath = System.getenv("SQLITE_DB_PATH") ?: "db.sqlite"
    Database.connect("jdbc:sqlite:$dbPath", driver = "org.sqlite.JDBC")
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
    configureRateLimit()
}

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
}

fun Application.configureRateLimit() {
    install(RateLimit) {
        global {
            rateLimiter(limit = 20, refillPeriod = 60.seconds)
        }
    }
}
