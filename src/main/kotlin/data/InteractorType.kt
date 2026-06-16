package org.data

import kotlinx.serialization.Serializable

@Serializable
enum class InteractorType {
    FilterParams,
    Image,
    Text,
    Float,
    FloatArray
}