package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Inspeccion(
    val codInspeccion: String? = null,
    val idPropuesta: String? = null,
    val estadoInspeccion: String? = "",
    val tokenInspeccion: String? = "",
    val duracionInspeccion: String? = "",
    val horaInicio: String? = "",
    val horaFinal: String? = "",
)