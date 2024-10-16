package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class SolicitudServicio(
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
