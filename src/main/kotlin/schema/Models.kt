package org.schema

import org.jetbrains.exposed.sql.Table

object Models : Table() {
    val id = integer("id")
        .autoIncrement()

    val name = varchar("name", 255)
        .uniqueIndex()

    val description = text("description")

    val size = long("size")

    val filePath = varchar("file_path", 500)

    override val primaryKey = PrimaryKey(id)
}