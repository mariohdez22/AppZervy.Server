package com.example.DTOs

import kotlinx.serialization.Serializable

@Serializable
data class SocioDTO(
    val idSocioDto: String? = null,
    val tipoSocioDto: String = "", // "Individual" o "Comercial"
    val idSocioIndividualDto: String? = null,
    val idSocioComercialDto: String? = null,
    val tipoServicioDto: String = "",
    val añosExperienciaDto: Int = 0,
    val puntajeGeneralDto: Double = 0.0
)
