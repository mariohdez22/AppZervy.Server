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

    override suspend fun crearCliente(cliente: Cliente): Cliente {

        val docRef = firestore.collection("clientes").document()
        val nuevoCliente = cliente.copy(idCliente = docRef.id)
        docRef.set(nuevoCliente).await()
        return nuevoCliente
    }

    override suspend fun obtenerClientes(): List<Cliente> {

        val snapshot = firestore.collection("clientes").get().await()
        return snapshot.documents.mapNotNull { document ->
            document.toObject(Cliente::class.java)?.copy(idCliente = document.id)
        }
    }

    override suspend fun obtenerClientePorId(id: String): Cliente? {

        val doc = firestore.collection("clientes").document(id).get().await()
        return if (doc.exists()) {
            doc.toObject(Cliente::class.java)?.copy(idCliente = doc.id)
        } else {
            null
        }
    }

    override suspend fun actualizarCliente(id: String, cliente: Cliente): Boolean {

        val docRef = firestore.collection("clientes").document(id)
        val updatedCliente = cliente.copy(idCliente = id)
        docRef.set(updatedCliente).await()
        return true
    }

    override suspend fun eliminarCliente(id: String): Boolean {

        firestore.collection("clientes").document(id).delete().await()
        return true
    }

}