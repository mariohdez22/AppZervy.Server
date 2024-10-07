package com.example.Mappers

import com.example.DTOs.DireccionDTO
import com.example.models.Direccion

fun DireccionDTO.toDireccion() : Direccion {

    return Direccion(
        idDireccion = this.idDireccion ?: "",
        idCliente = this.idCliente,
        direccion1 = this.direccion1,
        direccion2 = this.direccion2,
        numResidencia = this.numResidencia,
        codigoPostal = this.codigoPostal,
        pais = this.pais,
        departamento = this.departamento,
        ciudad = this.ciudad,
    )
}

fun Direccion.toDto() : DireccionDTO {

    return DireccionDTO(
        idDireccion = this.idDireccion ?: "",
        idCliente = this.idCliente,
        direccion1 = this.direccion1,
        direccion2 = this.direccion2,
        numResidencia = this.numResidencia,
        codigoPostal = this.codigoPostal,
        pais = this.pais,
        departamento = this.departamento,
        ciudad = this.ciudad,
    )
}