package com.example.DTOs

data class PagoServicioDTO(
    val idPagoServicioDTO: String? = null,
    val idClienteDTO: String? = null,
    val idSocioDTO: String? = null,
    val metodoPagoDTO: String = "",
    val estadoPagoDTO: String = "",
    val subtotalDTO: String = "",
    val impuestosDTO: String = "",
    val totalDTO: String = ""
)