package com.example.repository.clases

import com.example.models.MetodosPago
import com.example.repository.interfaces.IMetodosPagoRepository
import com.google.api.core.ApiFuture
import com.google.api.core.ApiFutureCallback
import com.google.api.core.ApiFutures
import com.google.cloud.firestore.Firestore
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class MetodosPagoRepository(private val firestore: Firestore) : IMetodosPagoRepository {

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

    override suspend fun crearMetodoPago(metodosPago: MetodosPago): MetodosPago {

        val clienteDoc = metodosPago.idCliente?.let { firestore.collection("clientes").document(it).get().await() }
        if (clienteDoc != null) {
            if (!clienteDoc.exists()) {
                throw IllegalArgumentException("El cliente con ID ${metodosPago.idCliente} no existe")
            }
        }

        val docRef = firestore.collection("metodospago").document()
        val nuevoMetodoPago = metodosPago.copy(idMetodoPago = docRef.id)
        docRef.set(nuevoMetodoPago).await()
        return nuevoMetodoPago
    }

    override suspend fun obtenerMetodoPagoPorCliente(idCliente: String): List<MetodosPago> {

        val snapshot = firestore.collection("metodospago")
            .whereEqualTo("idCliente", idCliente)
            .get()
            .await()

        return snapshot.documents.mapNotNull { document ->
            document.toObject(MetodosPago::class.java).copy(idMetodoPago = document.id)
        }
    }

    override suspend fun obtenerMetodoPagoPorId(idMetodoPago: String): MetodosPago? {

        val doc = firestore.collection("metodospago").document(idMetodoPago).get().await()
        return if (doc.exists()) {
            doc.toObject(MetodosPago::class.java)?.copy(idMetodoPago = doc.id)
        } else {
            null
        }
    }

    override suspend fun actualizarMetodosPago(idMetodoPago: String, metodosPago: MetodosPago): Boolean {

        val docRef = firestore.collection("metodospago").document(idMetodoPago)
        docRef.set(metodosPago).await()
        return true
    }

    override suspend fun eliminarMetodoPago(idMetodoPago: String): Boolean {

        firestore.collection("metodospago").document(idMetodoPago).delete().await()
        return true
    }


}