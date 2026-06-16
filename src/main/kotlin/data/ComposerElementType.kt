package org.data

import kotlinx.serialization.Serializable

@Serializable
enum class ComposerElementType {
    RunButton,
    ImageUpload,
    TextInput,
    FloatGauge,
}