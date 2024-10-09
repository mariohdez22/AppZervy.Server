package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Personal(
    val idPersonal : String? = null,
    val nombres: String = "",
    val celular: String = "",
    val correo: String = "",
    val contraseña: String = "",
    val foto: String = "",
    val tipoPersonal: String = "",
    val estadoPersonal: String = ""
)
