package org.dto

import kotlinx.serialization.Serializable

@Serializable
data class ModelInfoDTO(
    val name: String,
    val description: String,
    val size: Long,
)