package com.example.repository.clases

import com.example.models.Socio
import com.example.repository.interfaces.ISocioRepository
import com.google.cloud.firestore.Firestore
import com.google.api.core.ApiFuture
import com.google.api.core.ApiFutureCallback
import com.google.api.core.ApiFutures
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class SocioRepository(private val firestore: Firestore) : ISocioRepository {
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

    override suspend fun crearSocio(socio: Socio): Socio {
        val docRef = firestore.collection("socios").document()
        val nuevoSocio = socio.copy(idSocio = docRef.id)
        docRef.set(nuevoSocio).await()
        return nuevoSocio
    }

    override suspend fun obtenerSocios(): List<Socio> {
        val snapshot = firestore.collection("socios").get().await()
        return snapshot.documents.mapNotNull { document ->
            document.toObject(Socio::class.java)?.copy(idSocio = document.id)
        }
    }

    override suspend fun obtenerSocioPorId(id: String): Socio? {
        val doc = firestore.collection("socios").document(id).get().await()
        return if (doc.exists()) {
            doc.toObject(Socio::class.java)?.copy(idSocio = doc.id)
        } else {
            null
        }
    }

    override suspend fun actualizarSocio(id: String, socio: Socio): Boolean {
        val docRef = firestore.collection("socios").document(id)
        val updatedSocio = socio.copy(idSocio = id)
        docRef.set(updatedSocio).await()
        return true
    }

    override suspend fun eliminarSocio(id: String): Boolean {
        firestore.collection("socios").document(id).delete().await()
        return true
    }
}