package com.example.repository.clases

import com.example.models.SocioComercial
import com.example.repository.interfaces.ISocioComercialRepository
import com.google.cloud.firestore.Firestore
import com.google.api.core.ApiFuture
import com.google.api.core.ApiFutureCallback
import com.google.api.core.ApiFutures
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class SocioComercialRepository(private val firestore: Firestore) : ISocioComercialRepository {
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

    override suspend fun crearSocioComercial(socioComercial: SocioComercial): SocioComercial {
        val docRef = firestore.collection("sociosComerciales").document()
        val nuevoSocioComercial = socioComercial.copy(idSocioComercial = docRef.id)
        docRef.set(nuevoSocioComercial).await()
        return nuevoSocioComercial
    }

    override suspend fun obtenerSociosComerciales(): List<SocioComercial> {
        val snapshot = firestore.collection("sociosComerciales").get().await()
        return snapshot.documents.mapNotNull { document ->
            document.toObject(SocioComercial::class.java)?.copy(idSocioComercial = document.id)
        }
    }

    override suspend fun obtenerSocioComercialPorId(id: String): SocioComercial? {
        val doc = firestore.collection("sociosComerciales").document(id).get().await()
        return if (doc.exists()) {
            doc.toObject(SocioComercial::class.java)?.copy(idSocioComercial = doc.id)
        } else {
            null
        }
    }

    override suspend fun actualizarSocioComercial(id: String, socioComercial: SocioComercial): Boolean {
        val docRef = firestore.collection("sociosComerciales").document(id)
        val updatedSocioComercial = socioComercial.copy(idSocioComercial = id)
        docRef.set(updatedSocioComercial).await()
        return true
    }

    override suspend fun eliminarSocioComercial(id: String): Boolean {
        firestore.collection("sociosComerciales").document(id).delete().await()
        return true
    }
}