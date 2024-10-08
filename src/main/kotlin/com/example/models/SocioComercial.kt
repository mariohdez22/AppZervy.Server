package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class SocioComercial(
    val idSocioComercial: String? = null,
    val nombreComercial: String = "",
    val celularComercial: String = "",
    val correoComercial: String = "",
    val fotoComercial: String = "",
    val contraseña: String = "",
    val numeroIntegrantes: Int = 0,
    val nombreRepresentante: String = "",
    val fechaNacimiento: String = "", // Asumo que es una fecha, usar formateo adecuado
    val correo: String = "",
    val celular: String = "",
    val tipoDocumento: String = "",
    val documentoLegal: String = "",
    val estadoSocio: String = "" // Ejemplo: "Activo", "Inactivo"
)