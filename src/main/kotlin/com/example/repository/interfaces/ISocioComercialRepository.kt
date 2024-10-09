package com.example.repository.interfaces

import com.example.models.SocioComercial

interface ISocioComercialRepository {
    suspend fun crearSocioComercial(socioComercial: SocioComercial): SocioComercial
    suspend fun obtenerSociosComerciales(): List<SocioComercial>
    suspend fun obtenerSocioComercialPorId(id: String): SocioComercial?
    suspend fun actualizarSocioComercial(id: String, socioComercial: SocioComercial): Boolean
    suspend fun eliminarSocioComercial(id: String): Boolean
}