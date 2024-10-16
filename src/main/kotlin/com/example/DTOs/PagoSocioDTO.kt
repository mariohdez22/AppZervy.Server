package com.example.DTOs

import kotlinx.serialization.Serializable

@Serializable
data class PagoSocioDTO(
    val idPagoSocio: String? = null,
    val idSocio: String? = null,
    val numPago: Double = 0.0,
    val pagoSocioNeto: Double = 0.0,
    val comision: Double = 0.0,
    val pagoSocioLiquido: Double = 0.0
)
