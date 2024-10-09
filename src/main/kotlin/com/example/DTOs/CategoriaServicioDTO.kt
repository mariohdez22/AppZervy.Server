package com.example.DTOs

import kotlinx.serialization.Serializable

@Serializable
data class CategoriaServicioDTO(
    val idCategoriaServicio: String? = null,
    val foto: String = "",
    val tituloCategoria: String = "",
    val descripcion: String = "",
    val horarioServicio: String = "",
    val tipoCategoria: String = "",
)
