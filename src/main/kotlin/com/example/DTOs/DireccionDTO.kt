package com.example.DTOs

data class DireccionDTO(
    val idDireccion: String? = null,
    val idCliente: String,
    val direccion1: String = "",
    val direccion2: String = "",
    val numResidencia: String = "",
    val codigoPostal: String = "",
    val pais: String = "",
    val departamento: String = "",
    val ciudad: String = ""
)
