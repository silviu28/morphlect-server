package org.dto

import kotlinx.serialization.Serializable

// what the client receives
@Serializable
data class ModelInfoDTO(
    val id: Int,
    val name: String,
    val description: String,
    val size: Long,
)