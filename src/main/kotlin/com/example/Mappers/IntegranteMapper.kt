package com.example.Mappers

import com.example.DTOs.IntegrantesDTO
import com.example.models.Integrantes
//Hola
// Mapper de IntegranteDTO hacia Integrante (para métodos create, update y delete)
fun IntegrantesDTO.toIntegrante(): Integrantes {
    return Integrantes(
        idIntegrante = this.idIntegranteDto ?: "",
        idSocioComercial = this.idSocioComercialDto,
        nombres = this.nombresDto,
        celular = this.celularDto,
        correo = this.correoDto,
        fechaNacimiento = this.fechaNacimientoDto,
        edad = this.edadDto,
        tipoDocumento = this.tipoDocumentoDto,
        documentoLegal = this.documentoLegalDto
    )
}

// Mapper de Integrante hacia IntegranteDTO (para métodos read)
fun Integrantes.toDto(): IntegrantesDTO {
    return IntegrantesDTO(
        idIntegranteDto = this.idIntegrante ?: "",
        idSocioComercialDto = this.idSocioComercial,
        nombresDto = this.nombres,
        celularDto = this.celular,
        correoDto = this.correo,
        fechaNacimientoDto = this.fechaNacimiento,
        edadDto = this.edad,
        tipoDocumentoDto = this.tipoDocumento,
        documentoLegalDto = this.documentoLegal
    )
}