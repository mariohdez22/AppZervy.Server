package com.example.repository.interfaces

import com.example.models.Direccion

interface IDireccionRepository {

    suspend fun crearDireccion(direccion: Direccion): Direccion
    suspend fun obtenerDireccionesPorCliente(idCliente: String): List<Direccion>
    suspend fun obtenerDireccionPorId(idDireccion: String): Direccion?
    suspend fun actualizarDireccion(idDireccion: String, direccion: Direccion): Boolean
    suspend fun eliminarDireccion(idDireccion: String): Boolean

    // Nueva función para agregar dirección por Socio
    suspend fun agregarDireccionPorSocio(idSocio: String, direccion: Direccion): Direccion
}
