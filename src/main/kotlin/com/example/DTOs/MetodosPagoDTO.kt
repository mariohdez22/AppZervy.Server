package com.example.DTOs

import kotlinx.serialization.Serializable

@Serializable
data class MetodosPagoDTO(
    val idMetodoPago: String? = null,
    val idCliente: String? = null,
    val idSocio: String? = null,
    val idDireccion : String? = null,
    val tipoMetodo: String = "",
    val aliasTarjeta: String = "",
    val numTarjeta: String = "",
    val numCVV: String = "",
    val fechaVencimiento: String = "",
    val nombrePropietario: String = ""
)
