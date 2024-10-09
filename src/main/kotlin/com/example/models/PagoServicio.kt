package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class PagoServicio(
    val idPagoServicio: String? = null,
    val idCliente: String? = null,
    val idSocio: String? = null,
    val metodoPago: String = "",
    val estadoPago: String = "",
    val subtotal: String = "",
    val impuestos: String = "",
    val total: String = ""
)