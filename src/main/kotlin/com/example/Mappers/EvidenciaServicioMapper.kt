package com.example.Mappers

import com.example.DTOs.EvidenciaServicioDTO
import com.example.models.EvidenciaServicio

fun EvidenciaServicioDTO.toEvidenciaServicio() : EvidenciaServicio {

    return EvidenciaServicio(
        idEvidenciaServicio = this.idEvidenciaServicio ?: "",
        CodServicio = this.CodServicio,
        foto = this.foto,
        video = this.video,
        instaciaArchivo = this.instaciaArchivo,
        tipoArchivo = this.tipoArchivo
    )
}

fun EvidenciaServicio.toDto(): EvidenciaServicioDTO {

    return EvidenciaServicioDTO(
        idEvidenciaServicio = this.idEvidenciaServicio ?: "",
        CodServicio = this.CodServicio,
        foto = this.foto,
        video = this.video,
        tipoArchivo= this.tipoArchivo,
        instaciaArchivo = this.instaciaArchivo
    )
}