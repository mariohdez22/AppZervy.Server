package com.example.repository.clases

import com.example.models.SocioIndividual
import com.example.repository.interfaces.ISocioIndividualRepository
import com.google.cloud.firestore.Firestore
import com.google.api.core.ApiFuture
import com.google.api.core.ApiFutureCallback
import com.google.api.core.ApiFutures
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class SocioIndividualRepository(private val firestore: Firestore) : ISocioIndividualRepository {
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

    override suspend fun crearSocioIndividual(socioIndividual: SocioIndividual): SocioIndividual {
        val docRef = firestore.collection("sociosIndividuales").document()
        val nuevoSocioIndividual = socioIndividual.copy(idSocioIndividual = docRef.id)
        docRef.set(nuevoSocioIndividual).await()
        return nuevoSocioIndividual
    }

    override suspend fun obtenerSociosIndividuales(): List<SocioIndividual> {
        val snapshot = firestore.collection("sociosIndividuales").get().await()
        return snapshot.documents.mapNotNull { document ->
            document.toObject(SocioIndividual::class.java)?.copy(idSocioIndividual = document.id)
        }
    }

    override suspend fun obtenerSocioIndividualPorId(id: String): SocioIndividual? {
        val doc = firestore.collection("sociosIndividuales").document(id).get().await()
        return if (doc.exists()) {
            doc.toObject(SocioIndividual::class.java)?.copy(idSocioIndividual = doc.id)
        } else {
            null
        }
    }

    override suspend fun actualizarSocioIndividual(id: String, socioIndividual: SocioIndividual): Boolean {
        val docRef = firestore.collection("sociosIndividuales").document(id)
        val updatedSocioIndividual = socioIndividual.copy(idSocioIndividual = id)
        docRef.set(updatedSocioIndividual).await()
        return true
    }

    override suspend fun eliminarSocioIndividual(id: String): Boolean {
        firestore.collection("sociosIndividuales").document(id).delete().await()
        return true
    }
}