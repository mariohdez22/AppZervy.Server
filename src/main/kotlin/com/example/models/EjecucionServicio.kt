package com.example.models

data class EjecucionServicio(
    val codServicio: String? = null,
    val idPropuesta: String? = null,
    val idSocio: String? = null,
    val estadoServicio: String = "",
    val duracionServicio: String = "",
    val tokenServicio: String = "",
    val fechaInicio: String = "",
    val fechaFinal: String = "",
    val precioService: String = ""
)