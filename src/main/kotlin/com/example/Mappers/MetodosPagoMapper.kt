package com.example.Mappers

import com.example.DTOs.MetodosPagoDTO
import com.example.models.MetodosPago

fun MetodosPagoDTO.toMetodosPago(): MetodosPago {

    return MetodosPago(
        idMetodoPago = this.idMetodoPago ?: "",
        idCliente = this.idCliente ?: "",
        idSocio = this.idSocio ?: "",
        idDireccion = this.idDireccion ?: "",
        tipoMetodo = this.tipoMetodo,
        aliasTarjeta = this.aliasTarjeta,
        numTarjeta = this.numTarjeta,
        numCVV = this.numCVV,
        fechaVencimiento = this.fechaVencimiento,
        nombrePropietario = this.nombrePropietario
    )
}

fun MetodosPago.toDto(): MetodosPagoDTO {

    return MetodosPagoDTO(
        idMetodoPago = this.idMetodoPago ?: "",
        idCliente = this.idCliente ?: "",
        idSocio = this.idSocio ?: "",
        idDireccion = this.idDireccion ?: "",
        tipoMetodo = this.tipoMetodo,
        aliasTarjeta = this.aliasTarjeta,
        numTarjeta = this.numTarjeta,
        numCVV = this.numCVV,
        fechaVencimiento = this.fechaVencimiento,
        nombrePropietario = this.nombrePropietario
    )
}