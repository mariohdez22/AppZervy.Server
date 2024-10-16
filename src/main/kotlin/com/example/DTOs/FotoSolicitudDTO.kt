package com.example.DTOs

import kotlinx.serialization.Serializable

@Serializable
data class FotoSolicitudDTO(
    val idFotoSolicitud: String? = null,
    val idSolicitud: String? = null,
    val foto: String = "",
    val descripcionFoto: String = ""
)
