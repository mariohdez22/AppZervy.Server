package com.example.repository.interfaces

import com.example.models.CategoriaServicio
import com.example.models.Cliente

interface ICategoriaServicioRepository {

    suspend fun crearCategoriaServicio(categoriaServicio: CategoriaServicio): CategoriaServicio
    suspend fun obtenerCategoriaServicio(): List<CategoriaServicio>
    suspend fun obtenerCategoriaServicioPorId(id: String): CategoriaServicio?
    suspend fun actualizarCategoriaServicio(id: String, categoriaServicio: CategoriaServicio): Boolean
    suspend fun eliminarCategoriaServicio(id: String): Boolean
}