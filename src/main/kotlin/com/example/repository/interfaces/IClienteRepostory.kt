package com.example.repository.interfaces

import com.example.models.Cliente


interface IClienteRepostory {

    suspend fun crearCliente(cliente: Cliente): Cliente
    suspend fun obtenerClientes(): List<Cliente>
    suspend fun obtenerClientePorId(id: String): Cliente?
    suspend fun actualizarCliente(id: String, cliente: Cliente): Boolean
    suspend fun eliminarCliente(id: String): Boolean
}