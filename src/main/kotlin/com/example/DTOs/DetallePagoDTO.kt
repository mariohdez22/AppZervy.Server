package com.example.DTOs

import kotlinx.serialization.Serializable

@Serializable
data class DetallePagoDTO(
    val idDetalleDTO: String? = null,
    val idPagoServicioDTO: String? = null,
    val codInspeccionDTO: String? = null,
    val subTotalDTO: String = ""
)