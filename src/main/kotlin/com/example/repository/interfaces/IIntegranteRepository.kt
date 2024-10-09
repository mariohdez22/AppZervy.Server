package com.example.repository.interfaces

import com.example.models.Integrantes

interface IIntegranteRepository {
    suspend fun crearIntegrante(integrante: Integrantes): Integrantes
    suspend fun obtenerIntegrantes(): List<Integrantes>
    suspend fun obtenerIntegrantePorId(id: String): Integrantes?
    suspend fun actualizarIntegrante(id: String, integrante: Integrantes): Boolean
    suspend fun eliminarIntegrante(id: String): Boolean
}