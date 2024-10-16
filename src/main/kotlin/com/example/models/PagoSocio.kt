package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class PagoSocio(
    val idPagoSocio: String? = null,
    val idSocio: String? = null,
    val numPago: Double = 0.0,
    val pagoSocioNeto: Double = 0.0,
    val comision: Double = 0.0,
    val pagoSocioLiquido: Double = 0.0
)
