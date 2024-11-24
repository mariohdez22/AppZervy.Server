package com.example.DTOs

import kotlinx.serialization.Serializable

@Serializable
data class DireccionDTO(
    val idDireccion: String? = null,
    val idCliente: String? = null,
    val idSocio: String? = null,
    val direccion1: String = "",
    val direccion2: String = "",
    val numResidencia: String = "",
    val codigoPostal: String = "",
    val pais: String = "",
    val departamento: String = "",
    val ciudad: String = ""
)
