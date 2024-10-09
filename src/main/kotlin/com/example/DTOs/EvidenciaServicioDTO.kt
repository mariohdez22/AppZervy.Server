package com.example.DTOs
import kotlinx.serialization.Serializable

@Serializable
data class EvidenciaServicioDTO (
    val idEvidenciaServicio : String? = null,
    val CodServicio: String = "",
    val foto: String = "",
    val video: String = "",
    val instaciaArchivo: String = "",
    val tipoArchivo: String = ""
)