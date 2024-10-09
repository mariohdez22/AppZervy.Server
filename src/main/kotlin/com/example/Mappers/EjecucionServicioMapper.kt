package com.example.Mappers

import com.example.DTOs.EjecucionServicioDTO
import com.example.models.EjecucionServicio

fun EjecucionServicio.toEjecucionServicioDTO() : EjecucionServicioDTO {
    return EjecucionServicioDTO(
        codServicioDTO = codServicio,
        idPropuestaDTO =  idPropuesta,
        idSocioDTO =  idSocio,
        estadoServicioDTO = estadoServicio,
        duracionServicioDTO = duracionServicio,
        tokenServicioDTO = tokenServicio,
        fechaInicioDTO =  fechaInicio,
        fechaFinalDTO = fechaFinal,
        precioServiceDTO = precioService,
    )
}

fun EjecucionServicioDTO.toEjecucionServicio(): EjecucionServicio {
    return EjecucionServicio(
        codServicio = codServicioDTO,
        idPropuesta = idPropuestaDTO,
        idSocio = idSocioDTO,
        estadoServicio = estadoServicioDTO,
        duracionServicio = duracionServicioDTO,
        tokenServicio = tokenServicioDTO,
        fechaInicio = fechaInicioDTO,
        fechaFinal = fechaFinalDTO,
        precioService = precioServiceDTO,
    )
}