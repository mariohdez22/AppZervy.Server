package com.example.repository.interfaces

import com.example.models.FotoSolicitud

interface IFotoSolicitudRepository {

    suspend fun crearFotoSolicitud(fotoSolicitud: FotoSolicitud): FotoSolicitud
    suspend fun obtenerFotoSolicitudesPorSolicitud(idSolicitud: String): List<FotoSolicitud>
    suspend fun obtenerFotoSolicitudPorId(idFotoSolicitud: String): FotoSolicitud?
    suspend fun actualizarFotoSolicitud(idFotoSolicitud: String, fotoSolicitud: FotoSolicitud): Boolean
    suspend fun eliminarFotoSolicitud(idFotoSolicitud: String): Boolean
}