package com.example.DTOs

import kotlinx.serialization.Serializable

@Serializable
data class EjecucionServicioDTO(
    val codServicioDTO: String? = null,
    val idPropuestaDTO: String? = null,
    val idSocioDTO: String? = null,
    val estadoServicioDTO: String = "",
    val duracionServicioDTO: String = "",
    val tokenServicioDTO: String = "",
    val fechaInicioDTO: String = "",
    val fechaFinalDTO: String = "",
    val precioServiceDTO: String = ""
)