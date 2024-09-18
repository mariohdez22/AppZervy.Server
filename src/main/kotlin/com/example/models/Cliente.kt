package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Cliente(
    val idCliente: String? = null,
    val nombres: String = "",
    val celular: String = "",
    val correo: String = "",
    val contraseña: String = "",
    val foto: String = "",
    val dui: String = ""
)