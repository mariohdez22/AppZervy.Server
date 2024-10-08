package com.example.Mappers

import com.example.DTOs.SocioIndividualDTO
import com.example.models.SocioIndividual

// Mapper de SocioIndividualDTO hacia SocioIndividual (para métodos create, update y delete)
fun SocioIndividualDTO.toSocioIndividual(): SocioIndividual {
    return SocioIndividual(
        idSocioIndividual = this.idSocioIndividualDto ?: "",
        nombres = this.nombresDto,
        celular = this.celularDto,
        correo = this.correoDto,
        contraseña = this.contraseñaDto,
        foto = this.fotoDto,
        fechaNacimiento = this.fechaNacimientoDto,
        edad = this.edadDto,
        tipoDocumento = this.tipoDocumentoDto,
        documentoLegal = this.documentoLegalDto,
        estadoSocio = this.estadoSocioDto
    )
}

// Mapper de SocioIndividual hacia SocioIndividualDTO (para métodos read)
fun SocioIndividual.toDto(): SocioIndividualDTO {
    return SocioIndividualDTO(
        idSocioIndividualDto = this.idSocioIndividual ?: "",
        nombresDto = this.nombres,
        celularDto = this.celular,
        correoDto = this.correo,
        contraseñaDto = this.contraseña,
        fotoDto = this.foto,
        fechaNacimientoDto = this.fechaNacimiento,
        edadDto = this.edad,
        tipoDocumentoDto = this.tipoDocumento,
        documentoLegalDto = this.documentoLegal,
        estadoSocioDto = this.estadoSocio
    )
}