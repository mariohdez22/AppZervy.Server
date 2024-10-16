package com.example.repository.interfaces

import com.example.models.SolicitudServicio

interface ISolicitudServicioRepository {

    suspend fun crearSolicitudServicio(solicitudServicio: SolicitudServicio): SolicitudServicio
    suspend fun obtenerSolicitudServicioPorCliente(idCliente: String): List<SolicitudServicio>
    suspend fun obtenerSolicitudServicioPorId(idSolicitudServicio: String): SolicitudServicio?
    suspend fun actualizarSolicitudServicio(idSolicitudServicio: String, solicitudServicio: SolicitudServicio): Boolean
    suspend fun eliminarSolicitudServicio(idSolicitudServicio: String): Boolean
}