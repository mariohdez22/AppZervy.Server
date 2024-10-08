package com.example.Mappers

import com.example.DTOs.SocioDTO
import com.example.models.Socio

// Mapper de SocioDTO hacia Socio (para métodos create, update y delete)
fun SocioDTO.toSocio(): Socio {
    return Socio(
        idSocio = this.idSocioDto ?: "",
        tipoSocio = this.tipoSocioDto,
        idSocioIndividual = this.idSocioIndividualDto,
        idSocioComercial = this.idSocioComercialDto,
        tipoServicio = this.tipoServicioDto,
        añosExperiencia = this.añosExperienciaDto,
        puntajeGeneral = this.puntajeGeneralDto
    )
}

// Mapper de Socio hacia SocioDTO (para métodos read)
fun Socio.toDto(): SocioDTO {
    return SocioDTO(
        idSocioDto = this.idSocio ?: "",
        tipoSocioDto = this.tipoSocio,
        idSocioIndividualDto = this.idSocioIndividual,
        idSocioComercialDto = this.idSocioComercial,
        tipoServicioDto = this.tipoServicio,
        añosExperienciaDto = this.añosExperiencia,
        puntajeGeneralDto = this.puntajeGeneral
    )
}