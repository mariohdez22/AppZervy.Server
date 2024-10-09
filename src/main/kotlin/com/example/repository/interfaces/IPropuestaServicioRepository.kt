package com.example.repository.interfaces

import com.example.models.PropuestaServicio

interface IPropuestaServicioRepository {

    suspend fun crearPropuestaServicio(propuesta: PropuestaServicio) : PropuestaServicio
    suspend fun obtenerPropuestaServicio(): List<PropuestaServicio>
    suspend fun obtenerPropuestaServicioPorSocio(idSocio: String): List<PropuestaServicio>
    suspend fun obtenerPropuestaServicioPorSolicitud(idSolicitud: String): List<PropuestaServicio>
    suspend fun obtenerPropuestaServicioPorId(id: String): PropuestaServicio?
    suspend fun actualizarPropuestaServicio(id: String, propuesta: PropuestaServicio): Boolean
    suspend fun eliminarPropuestaServicio(id: String): Boolean

}