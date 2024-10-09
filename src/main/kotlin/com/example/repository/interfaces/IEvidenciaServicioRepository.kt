package com.example.repository.interfaces
import com.example.models.EvidenciaServicio

interface IEvidenciaServicioRepository {
    suspend fun crearEvidenciaServicio(evidenciaServicio: EvidenciaServicio): EvidenciaServicio
    suspend fun obtenerEvidenciaServicio(): List<EvidenciaServicio>
    suspend fun obtenerEvidenciaServicioPorId(id: String): EvidenciaServicio?
    suspend fun actualizarEvidenciaServicio(id: String, evidenciaServicio: EvidenciaServicio): Boolean
    suspend fun eliminarEvidenciaServicio(id: String): Boolean
}