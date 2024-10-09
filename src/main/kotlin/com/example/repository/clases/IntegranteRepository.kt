package com.example.repository.clases

import com.example.models.Integrantes
import com.example.repository.interfaces.IIntegranteRepository
import com.google.cloud.firestore.Firestore
import com.google.api.core.ApiFuture
import com.google.api.core.ApiFutureCallback
import com.google.api.core.ApiFutures
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class IntegranteRepository(private val firestore: Firestore) : IIntegranteRepository {
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

    override suspend fun crearIntegrante(integrante: Integrantes): Integrantes {
        val docRef = firestore.collection("integrantes").document()
        val nuevoIntegrante = integrante.copy(idIntegrante = docRef.id)
        docRef.set(nuevoIntegrante).await()
        return nuevoIntegrante
    }

    override suspend fun obtenerIntegrantes(): List<Integrantes> {
        val snapshot = firestore.collection("integrantes").get().await()
        return snapshot.documents.mapNotNull { document ->
            document.toObject(Integrantes::class.java)?.copy(idIntegrante = document.id)
        }
    }

    override suspend fun obtenerIntegrantePorId(id: String): Integrantes? {
        val doc = firestore.collection("integrantes").document(id).get().await()
        return if (doc.exists()) {
            doc.toObject(Integrantes::class.java)?.copy(idIntegrante = doc.id)
        } else {
            null
        }
    }

    override suspend fun actualizarIntegrante(id: String, integrante: Integrantes): Boolean {
        val docRef = firestore.collection("integrantes").document(id)
        val updatedIntegrante = integrante.copy(idIntegrante = id)
        docRef.set(updatedIntegrante).await()
        return true
    }

    override suspend fun eliminarIntegrante(id: String): Boolean {
        firestore.collection("integrantes").document(id).delete().await()
        return true
    }
}