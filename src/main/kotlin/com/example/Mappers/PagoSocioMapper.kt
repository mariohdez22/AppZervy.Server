package com.example.Mappers

import com.example.DTOs.PagoSocioDTO
import com.example.models.PagoSocio

fun PagoSocioDTO.toPagoSocio() : PagoSocio {

    return PagoSocio(

        idPagoSocio = this.idPagoSocio ?: "",
        idSocio = this.idSocio ?: "",
        numPago = this.numPago,
        pagoSocioNeto = this.pagoSocioNeto,
        comision = this.comision,
        pagoSocioLiquido = this.pagoSocioLiquido
    )
}

fun PagoSocio.toDto() : PagoSocioDTO {

    return PagoSocioDTO(

        idPagoSocio = this.idPagoSocio ?: "",
        idSocio = this.idSocio ?: "",
        numPago = this.numPago,
        pagoSocioNeto = this.pagoSocioNeto,
        comision = this.comision,
        pagoSocioLiquido = this.pagoSocioLiquido
    )
}