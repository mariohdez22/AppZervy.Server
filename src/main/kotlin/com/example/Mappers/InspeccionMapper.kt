package com.example.Mappers

import com.example.DTOs.InspeccionDTO
import com.example.models.Inspeccion

fun Inspeccion.toInspeccionDTO() : InspeccionDTO {

    return InspeccionDTO(
        codInspeccionDTO = codInspeccion,
        idPropuestaDTO = idPropuesta,
        estadoInspeccionDTO = estadoInspeccion,
        tokenInspeccionDTO = tokenInspeccion,
        duracionInspeccionDTO = duracionInspeccion,
        horaInicioDTO = horaInicio,
        horaFinalDTO = horaFinal,
    )
}

fun InspeccionDTO.toInspeccion() : Inspeccion {

    return Inspeccion(
        codInspeccion = codInspeccionDTO,
        idPropuesta = idPropuestaDTO,
        estadoInspeccion = estadoInspeccionDTO,
        tokenInspeccion = tokenInspeccionDTO,
        duracionInspeccion = duracionInspeccionDTO,
        horaInicio = horaInicioDTO,
        horaFinal = horaFinalDTO,
    )

}