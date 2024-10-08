package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class SocioIndividual(
    val idSocioIndividual: String? = null,
    val nombres: String = "",
    val celular: String = "",
    val correo: String = "",
    val contraseña: String = "",
    val foto: String = "",
    val fechaNacimiento: String = "", // Formatear adecuadamente
    val edad: Int = 0,
    val tipoDocumento: String = "",
    val documentoLegal: String = "",
    val estadoSocio: String = "" // Ejemplo: "Activo", "Inactivo"
)