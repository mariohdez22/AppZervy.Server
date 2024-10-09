package com.example.repository.interfaces

import com.example.models.Inspeccion

interface IInspeccionRepository {

    suspend fun crearInspeccion(inspeccion: Inspeccion): Inspeccion
    suspend fun obtenerInspecciones(): List<Inspeccion>
    suspend fun obtenerInspeccionPorPropuesta(idPropuesta: String): List<Inspeccion>
    suspend fun obtenerInspeccionPorId(codInspeccion: String): Inspeccion?
    suspend fun actualizarInspeccion(codInspeccion: String, inspeccion: Inspeccion): Boolean
    suspend fun eliminarInspeccion(codInspeccion: String): Boolean

}