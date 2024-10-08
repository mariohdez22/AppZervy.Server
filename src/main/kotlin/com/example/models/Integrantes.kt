package com.example.models

import kotlinx.serialization.Serializable


@Serializable
data class Integrantes (
    val idIntegrante: String? = null,
    val idSocioComercial: String = "",
    val nombres: String = "",
    val celular: String = "",
    val correo: String = "",
    val fechaNacimiento: String = "",
    val edad: Int = 0,
    val tipoDocumento: String = "",
    val documentoLegal: String = ""
)