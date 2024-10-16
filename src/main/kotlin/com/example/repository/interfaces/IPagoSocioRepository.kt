package com.example.repository.interfaces

import com.example.models.PagoSocio

interface IPagoSocioRepository {

    suspend fun crearPagoSocio(pagoSocio: PagoSocio): PagoSocio
    suspend fun obtenerPagoSocioPorSocio(idSocio: String): List<PagoSocio>
    suspend fun obtenerPagoSocioPorId(idPagoSocio: String): PagoSocio?
    suspend fun actualizarPagoSocio(idPagoSocio: String, pagoSocio: PagoSocio): Boolean
    suspend fun eliminarPagoSocio(idPagoSocio: String): Boolean
}