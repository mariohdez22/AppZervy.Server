package com.example.repository.interfaces
import com.example.models.Personal

interface IPersonalRepository {
    suspend fun crearPersonal(personal: Personal): Personal
    suspend fun obtenerPersonal(): List<Personal>
    suspend fun obtenerPersonalPorId(id: String): Personal?
    suspend fun actualizarPersonal(id: String, personal: Personal): Boolean
    suspend fun eliminarPersonal(id: String): Boolean
}