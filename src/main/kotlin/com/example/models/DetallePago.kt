package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class DetallePago(
    val idDetalle: String? = null,
    val idPagoServicio: String? = null,
    val codInspeccion: String? = null,
    val subTotal: String = ""
)