package com.example.Mappers

import com.example.DTOs.PagoServicioDTO
import com.example.models.PagoServicio

fun PagoServicio.toPagoServicioDTO() : PagoServicioDTO {

    return PagoServicioDTO(
        idPagoServicioDTO = idPagoServicio,
        idClienteDTO = idCliente ?: "",
        idSocioDTO = idSocio ?: "",
        metodoPagoDTO = metodoPago,
        estadoPagoDTO = estadoPago,
        subtotalDTO = subtotal,
        impuestosDTO = impuestos,
        totalDTO = total
    )

}

fun PagoServicioDTO.toPagoServicio() : PagoServicio {
    return PagoServicio(
        idPagoServicio = idPagoServicioDTO,
        idCliente = idClienteDTO,
        idSocio = idSocioDTO,
        metodoPago = metodoPagoDTO,
        estadoPago = estadoPagoDTO,
        subtotal = subtotalDTO,
        impuestos = impuestosDTO,
        total = totalDTO
    )
}