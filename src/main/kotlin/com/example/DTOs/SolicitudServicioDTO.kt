package com.example.DTOs

import kotlinx.serialization.Serializable

@Serializable
data class SolicitudServicioDTO(
    val idSolicitud: String? = null,
    val idCliente: String? = null,
    val idCategoriaServicio: String? = null,
    val nombreCliente: String = "",
    val foto: String = "",
    val tituloCategoria: String = "",
    val tipoCategoria: String = "",
    val tituloSolicitud: String = "",
    val descripcionSolicitud: String = "",
    val fechaSolicitud: String = "",
    val presupuesto: Double = 0.0,
    val tipoPago: String = "",
    val estadoSolicitud: String = ""
)
