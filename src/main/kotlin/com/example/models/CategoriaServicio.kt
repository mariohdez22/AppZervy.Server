package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class CategoriaServicio(
    val idCategoriaServicio: String? = null,
    val foto: String = "",
    val tituloCategoria: String = "",
    val descripcion: String = "",
    val horarioServicio: String = "",
    val tipoCategoria: String = "",
)
