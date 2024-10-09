package com.example.repository.interfaces

import com.example.models.Socio

interface ISocioRepository {
    suspend fun crearSocio(socio: Socio): Socio
    suspend fun obtenerSocios(): List<Socio>
    suspend fun obtenerSocioPorId(id: String): Socio?
    suspend fun actualizarSocio(id: String, socio: Socio): Boolean
    suspend fun eliminarSocio(id: String): Boolean
}