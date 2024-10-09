package com.example.models
import kotlinx.serialization.Serializable

@Serializable
data class EvidenciaServicio (

    val idEvidenciaServicio : String? = null,
    val CodServicio: String = "",
    val foto: String = "",
    val video: String = "",
    val instaciaArchivo: String = "",
    val tipoArchivo: String = ""
)