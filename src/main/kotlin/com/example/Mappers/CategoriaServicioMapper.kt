package com.example.Mappers

import com.example.DTOs.CategoriaServicioDTO
import com.example.models.CategoriaServicio

fun CategoriaServicioDTO.toCategoriaServicio() : CategoriaServicio {

    return CategoriaServicio(
        idCategoriaServicio = this.idCategoriaServicio ?: "",
        foto = this.foto,
        tituloCategoria = this.tituloCategoria,
        descripcion = this.descripcion,
        horarioServicio = this.horarioServicio,
        tipoCategoria = this.tipoCategoria
    )
}

fun CategoriaServicio.toDto() : CategoriaServicioDTO {

    return CategoriaServicioDTO(
        idCategoriaServicio = this.idCategoriaServicio ?: "",
        foto = this.foto,
        tituloCategoria = this.tituloCategoria,
        descripcion = this.descripcion,
        horarioServicio = this.horarioServicio,
        tipoCategoria = this.tipoCategoria
    )
}