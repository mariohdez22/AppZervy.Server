package com.example.DTOs

import kotlinx.serialization.Serializable

@Serializable
data class IntegrantesDTO(
    val idIntegranteDto: String? = null,
    val idSocioComercialDto: String = "",
    val nombresDto: String = "",
    val celularDto: String = "",
    val correoDto: String = "",
    val fechaNacimientoDto: String = "",
    val edadDto: Int = 0,
    val tipoDocumentoDto: String = "",
    val documentoLegalDto: String = ""
)
