package org

import io.ktor.server.application.Application
import io.ktor.server.application.log
import kotlinx.io.IOException
import kotlinx.serialization.decodeFromString
import net.mamoe.yamlkt.Yaml
import org.data.MXTManifest
import org.dto.InternalModelInfoDTO
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.transactions.transaction
import org.schema.Models
import java.io.File
import java.util.zip.ZipInputStream


fun searchDirectoryForModels(directoryPath: String): List<InternalModelInfoDTO> {
    val dir = File(directoryPath)
    val modelsInfo = mutableListOf<InternalModelInfoDTO>()
    if (!dir.isDirectory) return modelsInfo

    dir.listFiles()?.forEach { file ->
        if (file.extension == "mxt") {
            ZipInputStream(file.inputStream()).use { zip ->
                var curr = zip.nextEntry
                while (curr != null) {
                    if (curr.name == "extension_manifest.yml") {
                        val manifest = Yaml.decodeFromString<MXTManifest>(zip.bufferedReader().readText())
                        modelsInfo.add(
                            InternalModelInfoDTO(
                                name = file.name.removeSuffix(".mxt"),
                                description = manifest.description,
                                filePath = file.path,
                                size = file.length(),
                            )
                        )
                        return@use
                    }
                    curr = zip.nextEntry
                }
            }
        }
    }
    return modelsInfo
}

fun persistModels(models: List<InternalModelInfoDTO>) {
    transaction {
        SchemaUtils.create(Models)
        models.forEach { dto ->
            Models.insertIgnore {
                it[name] = dto.name
                it[description] = dto.description
                it[size] = dto.size
                it[filePath] = dto.filePath
            }
        }
    }
}

/**
 * create a record of all models stored in given directoryPath.
 */
fun Application.configureData(directoryPath: String = "models") {
    val modelsInfo = try {
        searchDirectoryForModels(directoryPath)
    } catch (e: IOException) {
        log.error(e.toString())
        emptyList()
    }
    persistModels(modelsInfo)
    log.info("Records updated in database.")
}