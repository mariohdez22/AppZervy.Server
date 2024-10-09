package com.example.repository.interfaces

import com.example.models.Direccion
import com.example.models.PagoServicio

interface IPagoServicioRepository {

    suspend fun crearPago(pagoServicio: PagoServicio): PagoServicio
    suspend fun obtenerPagos(): List<PagoServicio>
    suspend fun obtenerPagoPorCliente(idCliente: String): List<PagoServicio>
    suspend fun obtenerPagoPorSocio(idSocio: String): List<PagoServicio>
    suspend fun obtenerPagoPorId(idPagoServicio: String): PagoServicio?
    suspend fun actualizarPago(idPagoServicio: String, pagoServicio: PagoServicio): Boolean
    suspend fun eliminarPago(idPagoServicio: String): Boolean

}