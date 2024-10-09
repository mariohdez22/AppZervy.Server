package com.example.repository.interfaces
import com.example.models.Reseñas

interface IReseñasRepository {
    suspend fun crearReseñas(reseñas: Reseñas): Reseñas
    suspend fun obtenerReseñasPorCliente(idCliente: String): List<Reseñas>
    suspend fun obtenerReseñasPorSocio(idSocio: String): List<Reseñas>
    suspend fun obtenerReseñasPorId(id: String): Reseñas?
    suspend fun actualizarReseñas(id: String, reseñas: Reseñas): Boolean
    suspend fun eliminarReseñas(id: String): Boolean
}