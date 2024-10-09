package com.example.repository.clases

import com.example.models.EvidenciaServicio
import com.example.repository.interfaces.IEvidenciaServicioRepository
import com.google.cloud.firestore.Firestore
import com.google.api.core.ApiFuture
import com.google.api.core.ApiFutureCallback
import com.google.api.core.ApiFutures
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


class EvidenciaServicioRepository (private val firestore: Firestore) : IEvidenciaServicioRepository {

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

    override suspend fun crearEvidenciaServicio(evidenciaServicio: EvidenciaServicio): EvidenciaServicio {

        val docRef = firestore.collection("evidenciaServicio").document()
        val nuevoEvidenciaServicio = evidenciaServicio.copy(idEvidenciaServicio = docRef.id)
        docRef.set(nuevoEvidenciaServicio).await()
        return nuevoEvidenciaServicio
    }

    override suspend fun obtenerEvidenciaServicio(): List<EvidenciaServicio> {

        val snapshot = firestore.collection("evidenciaServicio").get().await()
        return snapshot.documents.mapNotNull { document ->
            document.toObject(EvidenciaServicio::class.java)?.copy(idEvidenciaServicio = document.id)
        }
    }

    override suspend fun obtenerEvidenciaServicioPorId(id: String): EvidenciaServicio? {

        val doc = firestore.collection("evidenciaServicio").document(id).get().await()
        return if (doc.exists()) {
            doc.toObject(EvidenciaServicio::class.java)?.copy(idEvidenciaServicio = doc.id)
        } else {
            null
        }
    }

    override suspend fun actualizarEvidenciaServicio(id: String, evidenciaServicio: EvidenciaServicio): Boolean {

        val docRef = firestore.collection("evidenciaServicio").document(id)
        val updatedEvidenciaServicio = evidenciaServicio.copy(idEvidenciaServicio = id)
        docRef.set(updatedEvidenciaServicio).await()
        return true
    }

    override suspend fun eliminarEvidenciaServicio(id: String): Boolean {

        firestore.collection("evidenciaServicio").document(id).delete().await()
        return true
    }









}