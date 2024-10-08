package com.example.DTOs

import kotlinx.serialization.Serializable

@Serializable
data class InspeccionDTO(
    val codInspeccionDTO: String? = null,
    val idPropuestaDTO: String? = null,
    val estadoInspeccionDTO: String? = "",
    val tokenInspeccionDTO: String? = "",
    val duracionInspeccionDTO: String? = "",
    val horaInicioDTO: String? = "",
    val horaFinalDTO: String? = "",
)