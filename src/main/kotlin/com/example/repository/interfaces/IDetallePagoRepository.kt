package com.example.repository.interfaces

import com.example.models.DetallePago
import com.example.models.PagoServicio

interface IDetallePagoRepository {

    suspend fun crearDetalle(detallePago: DetallePago): DetallePago
    suspend fun obtenerDetalles(): List<DetallePago>
    suspend fun obtenerDetallePorServicio(idPagoServicio: String): List<DetallePago>
    suspend fun obtenerDetallePorInspeccion(codInspeccion: String): List<DetallePago>
    suspend fun obtenerDetallePorId(idDetalle: String): DetallePago?
    suspend fun actualizarDetalle(idDetalle: String, detallePago: DetallePago): Boolean
    suspend fun eliminarDetalle(idDetalle: String): Boolean

}