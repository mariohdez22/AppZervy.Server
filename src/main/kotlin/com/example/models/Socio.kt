package com.example.models

import kotlinx.serialization.Serializable
//Hola
@Serializable
data class Socio(
    val idSocio: String? = null,
    val tipoSocio: String = "", // "Individual" o "Comercial"
    val idSocioIndividual: String? = null,
    val idSocioComercial: String? = null,
    val tipoServicio: String = "",
    val añosExperiencia: Int = 0,
    val puntajeGeneral: Double = 0.0
)