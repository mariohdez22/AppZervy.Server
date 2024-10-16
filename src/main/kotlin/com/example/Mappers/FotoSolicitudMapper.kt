package com.example.Mappers

import com.example.DTOs.FotoSolicitudDTO
import com.example.models.FotoSolicitud

fun FotoSolicitudDTO.toFotoSolicitud(): FotoSolicitud {

    return FotoSolicitud(

        idFotoSolicitud = this.idFotoSolicitud ?: "",
        idSolicitud = this.idSolicitud ?: "",
        foto = this.foto,
        descripcionFoto = this.descripcionFoto
    )
}

fun FotoSolicitud.toDto(): FotoSolicitudDTO {

    return FotoSolicitudDTO(

        idFotoSolicitud = this.idFotoSolicitud ?: "",
        idSolicitud = this.idSolicitud ?: "",
        foto = this.foto,
        descripcionFoto = this.descripcionFoto
    )
}