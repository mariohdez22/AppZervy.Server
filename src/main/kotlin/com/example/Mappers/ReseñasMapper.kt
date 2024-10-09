package com.example.Mappers


import com.example.DTOs.PersonalDTO
import com.example.models.Reseñas
import com.example.DTOs.ReseñasDTO
import com.example.models.Personal


fun ReseñasDTO.toReseñas() : Reseñas {

    return Reseñas(
        idReseñas = this.idReseñas ?: "",
        idInscripccion = this.idInscripccion ?: "",
        idSocio = this.idSocio ?: "",
        idCliente = this.idCliente ?: "",
        tituloReseña = this.tituloReseña,
        cuerpoReseña = this.cuerpoReseña ,
        fechaReseña = this.fechaReseña,
        puntuajeReseña = this.puntuajeReseña
    )
}

fun Reseñas.toDto(): ReseñasDTO {

    return ReseñasDTO(
        idReseñas= this.idReseñas ?: "",
        idInscripccion = this.idInscripccion ?: "",
        idSocio = this.idSocio ?: "",
        idCliente = this.idCliente ?: "",
        tituloReseña = this.tituloReseña,
        cuerpoReseña = this.cuerpoReseña,
        fechaReseña = this.fechaReseña,
        puntuajeReseña = this.puntuajeReseña
    )
}