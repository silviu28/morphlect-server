package org

import io.ktor.http.ContentDisposition
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.dto.ModelInfoDTO
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.schema.Models
import java.io.File

fun Application.configureRouting() {
    routing {
        // show all model data
        get("/api/models") {
            val query = call.request.queryParameters["query"]
            val limit = call.request.queryParameters["limit"]?.toInt() ?: 10
            val page = call.request.queryParameters["page"]?.toLong() ?: 0

            val data = transaction {
                if (query.isNullOrBlank()) {
                    Models.selectAll()
                        .limit(limit, (page - 1) * limit)
                        .map {
                        ModelInfoDTO(
                            name = it[Models.name],
                            description = it[Models.description],
                            size = it[Models.size],
                        )
                    }
                } else {
                    Models.selectAll()
                        .where { Models.name like "%$query%" }
                        .limit(limit, (page - 1) * limit)
                        .map {
                            ModelInfoDTO(
                                name = it[Models.name],
                                description = it[Models.description],
                                size = it[Models.size],
                            )
                        }
                }
            }
            call.respond(data)
        }

        // show data for selected model
        get("/api/models/{id}") {
            val id = call.parameters["id"]?.toInt()
                ?: return@get call.respond(HttpStatusCode.BadRequest)

            val data = transaction {
                Models.selectAll()
                    .where { Models.id eq id }
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

        get("/api/models/{id}/download") {
            val id = call.parameters["id"]?.toInt()
                ?: return@get call.respond(HttpStatusCode.BadRequest)

            val (modelName, filePath) = transaction {
                Models.selectAll()
                    .where { Models.id eq id }
                    .singleOrNull()
                    ?.let { Pair(it[Models.name], it[Models.filePath]) }
            } ?: return@get call.respond(HttpStatusCode.NotFound)

            val file = File(filePath)
            if (!file.exists()) {
                return@get call.respond(HttpStatusCode.NotFound)
            }

            call.response.header(
                HttpHeaders.ContentDisposition,
                ContentDisposition.Attachment
                    .withParameter(
                        ContentDisposition.Parameters.FileName,
                        "$modelName.tflite"
                    )
                    .toString()
            )
            call.response.header(HttpHeaders.ContentType, "application/octet-stream")
            call.respondFile(file)
        }
    }
}