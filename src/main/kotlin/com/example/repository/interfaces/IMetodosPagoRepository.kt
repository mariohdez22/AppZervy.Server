package com.example.repository.interfaces

import com.example.models.MetodosPago

interface IMetodosPagoRepository {

    suspend fun crearMetodoPago(metodosPago: MetodosPago): MetodosPago
    suspend fun obtenerMetodoPagoPorCliente(idCliente: String): List<MetodosPago>
    suspend fun obtenerMetodoPagoPorId(idMetodoPago: String): MetodosPago?
    suspend fun actualizarMetodosPago(idMetodoPago: String, metodosPago: MetodosPago): Boolean
    suspend fun eliminarMetodoPago(idMetodoPago: String): Boolean
}