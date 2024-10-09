package com.example.DTOs
import kotlinx.serialization.Serializable

@Serializable
data class ReseñasDTO (
    val idReseñas : String? = null,
    val idInscripccion: String ? = null,
    val idSocio: String ? = null,
    val idCliente: String ? = null,
    val tituloReseña: String = "",
    val cuerpoReseña: String = "",
    val fechaReseña: String = "",
    val puntuajeReseña: String = ""
)