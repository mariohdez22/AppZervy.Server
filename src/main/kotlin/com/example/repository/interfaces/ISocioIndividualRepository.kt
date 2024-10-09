package com.example.repository.interfaces

import com.example.models.SocioIndividual

interface ISocioIndividualRepository {
    suspend fun crearSocioIndividual(socioIndividual: SocioIndividual): SocioIndividual
    suspend fun obtenerSociosIndividuales(): List<SocioIndividual>
    suspend fun obtenerSocioIndividualPorId(id: String): SocioIndividual?
    suspend fun actualizarSocioIndividual(id: String, socioIndividual: SocioIndividual): Boolean
    suspend fun eliminarSocioIndividual(id: String): Boolean
}