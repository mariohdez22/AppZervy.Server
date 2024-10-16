package com.example.repository.clases

import com.example.models.Cliente
import com.example.repository.interfaces.IClienteRepostory
import com.google.cloud.firestore.Firestore
import com.google.api.core.ApiFuture
import com.google.api.core.ApiFutureCallback
import com.google.api.core.ApiFutures
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class ClienteRepository(private val firestore: Firestore) : IClienteRepostory {

    // Función de extensión para ApiFuture<T>
    suspend fun <T> ApiFuture<T>.await(): T = suspendCancellableCoroutine { cont ->
        ApiFutures.addCallback(this, object : ApiFutureCallback<T> {
            override fun onSuccess(result: T) {
                if (cont.isActive) {
                    cont.resume(result)
                }
            }

            override fun onFailure(t: Throwable) {
                if (cont.isActive) {
                    cont.resumeWithException(t)
                }
            }
        }, Runnable::run)
    }

    // Crear un nuevo cliente usando el uid como idCliente
    override suspend fun crearCliente(cliente: Cliente): Cliente {
        val docRef = firestore.collection("clientes").document(cliente.idCliente!!)
        docRef.set(cliente).await()
        return cliente
    }

    // Obtener todos los clientes (no es necesario filtrar por uid, ya que cada documento es único por uid)
    override suspend fun obtenerClientes(): List<Cliente> {
        val snapshot = firestore.collection("clientes").get().await()
        return snapshot.documents.mapNotNull { document ->
            document.toObject(Cliente::class.java)?.copy(idCliente = document.id)
        }
    }

    // Obtener un cliente por idCliente (uid)
    override suspend fun obtenerClientePorId(id: String): Cliente? {
        val doc = firestore.collection("clientes").document(id).get().await()
        return if (doc.exists()) {
            doc.toObject(Cliente::class.java)?.copy(idCliente = doc.id)
        } else {
            null
        }
    }

    // Actualizar un cliente si existe
    override suspend fun actualizarCliente(id: String, cliente: Cliente): Boolean {
        val clienteExistente = obtenerClientePorId(id)
        return if (clienteExistente != null) {
            firestore.collection("clientes").document(id).set(cliente).await()
            true
        } else {
            false
        }
    }

    // Eliminar un cliente si existe
    override suspend fun eliminarCliente(id: String): Boolean {
        val clienteExistente = obtenerClientePorId(id)
        return if (clienteExistente != null) {
            firestore.collection("clientes").document(id).delete().await()
            true
        } else {
            false
        }
    }

}