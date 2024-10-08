package com.example.repository.interfaces

import com.example.models.Inspeccion

interface IInspeccionRepository {

    suspend fun crearInspeccion(inspeccion: Inspeccion): Inspeccion
    suspend fun obtenerInspeccionPorPropuesta(idPropuesta: String): List<Inspeccion>
    suspend fun obtenerInspeccionPorId(idInspeccion: String): Inspeccion?
    suspend fun actualizarInspeccion(idInspeccion: String, inspeccion: Inspeccion): Boolean
    suspend fun eliminarInspeccion(idInspeccion: String): Boolean

}