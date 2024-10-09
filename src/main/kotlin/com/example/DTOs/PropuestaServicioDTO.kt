package com.example.DTOs

import kotlinx.serialization.Serializable

@Serializable
data class PropuestaServicioDTO(
    val idPropuestaDTO: String? = null,
    val idSocioDTO: String? = null,
    val idSolicitudDTO: String? = null,
    val nombreClienteDTO: String = "",
    val tituloCategoriaDTO: String = "",
    val fotoDTO: String = "",
    val presupuestoDTO: String? = null,
    val tipoPagoDTO: String = "",
    val tipoCategoriaDTO: String = "",
    val tituloPropuestaDTO: String = "",
    val descripcionPropuestaDTO: String = "",
    val duracionServicioDTO: String = "",
    val estadoPropuestaDTO: String = "",
    val precioBaseDTO: String = "",
)