package com.example.repository.clases

import com.example.models.Direccion
import com.example.repository.interfaces.IDireccionRepository
import com.google.api.core.ApiFuture
import com.google.api.core.ApiFutureCallback
import com.google.api.core.ApiFutures
import com.google.cloud.firestore.Firestore
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class DireccionRepository(private val firestore: Firestore) : IDireccionRepository {

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

    override suspend fun crearDireccion(direccion: Direccion): Direccion {

        val clienteDoc = direccion.idCliente?.let { firestore.collection("clientes").document(it).get().await() }
        if (clienteDoc != null) {
            if (!clienteDoc.exists()) {
                throw IllegalArgumentException("El cliente con ID ${direccion.idCliente} no existe")
            }
        }

        val docRef = firestore.collection("direcciones").document()
        val nuevaDireccion = direccion.copy(idDireccion = docRef.id)
        docRef.set(nuevaDireccion).await()
        return nuevaDireccion
    }

    override suspend fun obtenerDireccionesPorCliente(idCliente: String): List<Direccion> {

        val snapshot = firestore.collection("direcciones")
            .whereEqualTo("idCliente", idCliente)
            .get()
            .await()
        return snapshot.documents.mapNotNull { document ->
            document.toObject(Direccion::class.java).copy(idDireccion = document.id)
        }
    }

    override suspend fun obtenerDireccionPorId(idDireccion: String): Direccion? {

        val doc = firestore.collection("direcciones").document(idDireccion).get().await()
        return if (doc.exists()) {
            doc.toObject(Direccion::class.java)?.copy(idDireccion = doc.id)
        } else {
            null
        }
    }

    override suspend fun actualizarDireccion(idDireccion: String, direccion: Direccion): Boolean {

        val docRef = firestore.collection("direcciones").document(idDireccion)
        docRef.set(direccion).await()
        return true
    }

    override suspend fun eliminarDireccion(idDireccion: String): Boolean {

        firestore.collection("direcciones").document(idDireccion).delete().await()
        return true
    }


}