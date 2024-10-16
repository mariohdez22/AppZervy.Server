package com.example.Mappers

import com.example.DTOs.SolicitudServicioDTO
import com.example.models.SolicitudServicio

fun SolicitudServicioDTO.toSolicitudServicio(): SolicitudServicio {

    return SolicitudServicio(

        idSolicitud = this.idSolicitud ?: "",
        idCliente = this.idCliente ?: "",
        idCategoriaServicio = this.idCategoriaServicio ?: "",
        nombreCliente = this.nombreCliente,
        foto = this.foto,
        tituloCategoria = this.tituloCategoria,
        tipoCategoria = this.tipoCategoria,
        tituloSolicitud = this.tituloSolicitud,
        descripcionSolicitud = this.descripcionSolicitud,
        fechaSolicitud = this.fechaSolicitud,
        presupuesto = this.presupuesto,
        tipoPago  = this.tipoPago,
        estadoSolicitud = this.estadoSolicitud
    )
}

fun SolicitudServicio.toDTO(): SolicitudServicioDTO {

    return SolicitudServicioDTO(

        idSolicitud = this.idSolicitud ?: "",
        idCliente = this.idCliente ?: "",
        idCategoriaServicio = this.idCategoriaServicio ?: "",
        nombreCliente = this.nombreCliente,
        foto = this.foto,
        tituloCategoria = this.tituloCategoria,
        tipoCategoria = this.tipoCategoria,
        tituloSolicitud = this.tituloSolicitud,
        descripcionSolicitud = this.descripcionSolicitud,
        fechaSolicitud = this.fechaSolicitud,
        presupuesto = this.presupuesto,
        tipoPago  = this.tipoPago,
        estadoSolicitud = this.estadoSolicitud
    )
}
