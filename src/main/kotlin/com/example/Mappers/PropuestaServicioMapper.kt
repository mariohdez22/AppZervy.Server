package com.example.Mappers

import com.example.DTOs.PropuestaServicioDTO
import com.example.models.PropuestaServicio

fun PropuestaServicioDTO.toPropuestaServicio() : PropuestaServicio {

    return PropuestaServicio(
        idPropuesta = this.idPropuestaDTO,
        idSocio = this.idSocioDTO,
        idSolicitud = this.idSolicitudDTO,
        nombreCliente = this.nombreClienteDTO,
        tituloCategoria = this.tituloCategoriaDTO,
        foto = this.fotoDTO,
        presupuesto = this.presupuestoDTO,
        tipoPago = this.tipoPagoDTO,
        tipoCategoria = this.tipoCategoriaDTO,
        tituloPropuesta = this.tituloPropuestaDTO,
        descripcionPropuesta = this.descripcionPropuestaDTO,
        duracionServicio = this.duracionServicioDTO,
        estadoPropuesta = this.estadoPropuestaDTO,
        precioBase = this.precioBaseDTO,
    )
}

fun PropuestaServicio.toPropuestaServicioDTO() : PropuestaServicioDTO {

    return PropuestaServicioDTO(
        idPropuestaDTO = this.idPropuesta,
        idSocioDTO = this.idSocio,
        idSolicitudDTO = this.idSolicitud,
        nombreClienteDTO = this.nombreCliente,
        tituloCategoriaDTO = this.tituloCategoria,
        fotoDTO = this.foto,
        presupuestoDTO = this.presupuesto,
        tipoPagoDTO = this.tipoPago,
        tipoCategoriaDTO = this.tipoCategoria,
        tituloPropuestaDTO = this.tituloPropuesta,
        descripcionPropuestaDTO = this.descripcionPropuesta,
        duracionServicioDTO = this.duracionServicio,
        estadoPropuestaDTO = this.estadoPropuesta,
        precioBaseDTO = this.precioBase,
    )
}