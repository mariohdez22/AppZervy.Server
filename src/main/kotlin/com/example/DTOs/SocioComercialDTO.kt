package com.example.DTOs

import kotlinx.serialization.Serializable
//Hola
@Serializable
data class SocioComercialDTO(
    val idSocioComercialDto: String? = null,
    val nombreComercialDto: String = "",
    val celularComercialDto: String = "",
    val correoComercialDto: String = "",
    val fotoComercialDto: String = "",
    val contraseñaDto: String = "",
    val numeroIntegrantesDto: Int = 0,
    val nombreRepresentanteDto: String = "",
    val fechaNacimientoDto: String = "", // Fecha formateada
    val correoDto: String = "",
    val celularDto: String = "",
    val tipoDocumentoDto: String = "",
    val documentoLegalDto: String = "",
    val estadoSocioDto: String = "" // Ejemplo: "Activo", "Inactivo"
)
