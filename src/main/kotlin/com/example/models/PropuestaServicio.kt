package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class PropuestaServicio (
    val idPropuesta: String? = null,
    val idSocio: String? = null,
    val idSolicitud: String? = null,
    val nombreCliente: String = "",
    val tituloCategoria: String = "",
    val foto: String = "",
    val presupuesto: String? = null,
    val tipoPago: String = "",
    val tipoCategoria: String = "",
    val tituloPropuesta: String = "",
    val descripcionPropuesta: String = "",
    val duracionServicio: String = "",
    val estadoPropuesta: String = "",
    val precioBase: String = "",
)