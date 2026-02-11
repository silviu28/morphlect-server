package org

import io.ktor.server.application.Application
import io.ktor.server.application.log
import kotlinx.io.IOException
import org.dto.InternalModelInfoDTO
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.transactions.transaction
import org.schema.Models
import org.sqlite.SQLiteException
import java.io.File

/**
 * for a given subdirectory returns model information if it is of valid structure.
 * */
private fun retrieveModelData(modelSubdir: File): InternalModelInfoDTO? {
    if (!modelSubdir.isDirectory) {
        throw IOException("Given file reference is not a subdirectory.")
    }
    for (file in modelSubdir.listFiles()) {
        if (file.name.equals(".meta")) {
            val tokens = file.readText().split("\r\n")
            return InternalModelInfoDTO(
                name = tokens[0],
                description = tokens[1],
                size = file.length(),
                filePath = file.path,
            )
        }
    }
    return null
}

/**
 * create a record of all models stored in given directoryPath.
 */
fun Application.configureData(directoryPath: String = "models") {
    val dir = File(directoryPath)
    val modelsInfo = mutableListOf<InternalModelInfoDTO>()

    try {
        if (dir.isDirectory) {
            dir.listFiles().forEach { subdir ->
                if (subdir.isDirectory) {
                    retrieveModelData(subdir)?.let { modelsInfo.add(it) }
                }
            }
        }
    } catch (e: IOException) {
        println(e.toString())
    }

    // update db records
    transaction {
        SchemaUtils.create(Models)
        modelsInfo.forEach { dto ->
            Models.insertIgnore {
                it[name] = dto.name
                it[description] = dto.description
                it[size] = dto.size
                it[filePath] = dto.filePath
            }
        }
    }

    log.info("Records updated in database.")
}