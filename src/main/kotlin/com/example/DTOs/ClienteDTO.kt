package com.example.DTOs

import kotlinx.serialization.Serializable

@Serializable
data class ClienteDTO (
    val idClienteDto: String? = null,
    val nombresDto: String = "",
    val celularDto: String = "",
    val correoDto: String = "",
    val contraseñaDto: String = "",
    val fotoDto: String = "",
    val duiDto: String = ""
)