package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class FotoSolicitud(
    val idFotoSolicitud: String? = null,
    val idSolicitud: String? = null,
    val foto: String = "",
    val descripcionFoto: String = ""
)
