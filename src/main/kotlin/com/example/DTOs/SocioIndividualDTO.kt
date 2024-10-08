package com.example.DTOs

import kotlinx.serialization.Serializable

@Serializable
data class SocioIndividualDTO(
    val idSocioIndividualDto: String? = null,
    val nombresDto: String = "",
    val celularDto: String = "",
    val correoDto: String = "",
    val contraseñaDto: String = "",
    val fotoDto: String = "",
    val fechaNacimientoDto: String = "", // Formatear adecuadamente
    val edadDto: Int = 0,
    val tipoDocumentoDto: String = "",
    val documentoLegalDto: String = "",
    val estadoSocioDto: String = "" // Ejemplo: "Activo", "Inactivo"
)
