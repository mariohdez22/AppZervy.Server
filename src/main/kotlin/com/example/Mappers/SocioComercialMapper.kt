package com.example.Mappers

import com.example.DTOs.SocioComercialDTO
import com.example.models.SocioComercial
//Hola
// Mapper de SocioComercialDTO hacia SocioComercial (para métodos create, update y delete)
fun SocioComercialDTO.toSocioComercial(): SocioComercial {
    return SocioComercial(
        idSocioComercial = this.idSocioComercialDto ?: "",
        nombreComercial = this.nombreComercialDto,
        celularComercial = this.celularComercialDto,
        correoComercial = this.correoComercialDto,
        fotoComercial = this.fotoComercialDto,
        contraseña = this.contraseñaDto,
        numeroIntegrantes = this.numeroIntegrantesDto,
        nombreRepresentante = this.nombreRepresentanteDto,
        fechaNacimiento = this.fechaNacimientoDto,
        correo = this.correoDto,
        celular = this.celularDto,
        tipoDocumento = this.tipoDocumentoDto,
        documentoLegal = this.documentoLegalDto,
        estadoSocio = this.estadoSocioDto
    )
}

// Mapper de SocioComercial hacia SocioComercialDTO (para métodos read)
fun SocioComercial.toDto(): SocioComercialDTO {
    return SocioComercialDTO(
        idSocioComercialDto = this.idSocioComercial ?: "",
        nombreComercialDto = this.nombreComercial,
        celularComercialDto = this.celularComercial,
        correoComercialDto = this.correoComercial,
        fotoComercialDto = this.fotoComercial,
        contraseñaDto = this.contraseña,
        numeroIntegrantesDto = this.numeroIntegrantes,
        nombreRepresentanteDto = this.nombreRepresentante,
        fechaNacimientoDto = this.fechaNacimiento,
        correoDto = this.correo,
        celularDto = this.celular,
        tipoDocumentoDto = this.tipoDocumento,
        documentoLegalDto = this.documentoLegal,
        estadoSocioDto = this.estadoSocio
    )
}