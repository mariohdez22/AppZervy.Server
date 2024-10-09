package com.example.repository.interfaces

import com.example.models.EjecucionServicio

interface IEjecucionServicioRepository {

    suspend fun crearEjecucionServicio(ejServicio: EjecucionServicio): EjecucionServicio
    suspend fun obtenerEjecucionPorPropuesta(idPropuesta: String): List<EjecucionServicio>
    suspend fun obtenerEjecucionPorSocio(idSocio: String): List<EjecucionServicio>
    suspend fun obtenerEjecucionPorId(codServicio: String): EjecucionServicio?
    suspend fun actualizarEjecucion(codServicio: String, ejServicio: EjecucionServicio): Boolean
    suspend fun eliminarEjecucion(codServicio: String): Boolean

}