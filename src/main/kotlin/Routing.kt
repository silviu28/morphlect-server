package org

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.dto.ModelInfoDTO
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.schema.Models

fun Application.configureRouting() {
    routing {
        // show all model data
        get("/api/models") {
            val data = transaction {
                Models.selectAll().map {
                    ModelInfoDTO(
                        name = it[Models.name],
                        description = it[Models.description],
                        size = it[Models.size],
                    )
                }
            }
            call.respond(data)
        }

        get("/api/models/{id}") {
            val id = call.parameters["id"]?.toInt()
                ?: return@get call.respond(HttpStatusCode.BadRequest)
            val data = transaction {
                Models.select { Models.id eq id }
                    .singleOrNull()
                    ?.let {
                        ModelInfoDTO(
                            name = it[Models.name],
                            description = it[Models.description],
                            size = it[Models.size],
                        )
                    }
            }
            call.respond(data ?: HttpStatusCode.NotFound)
        }
    }
}