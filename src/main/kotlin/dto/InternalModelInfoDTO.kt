package org.dto

import kotlinx.serialization.Serializable

// what is used by the server, to pass along data
@Serializable
data class InternalModelInfoDTO(
    val name: String,
    val description: String,
    val size: Long,
    val filePath: String,
)