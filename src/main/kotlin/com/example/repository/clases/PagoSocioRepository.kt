package com.example.repository.clases

import com.example.models.PagoSocio
import com.example.repository.interfaces.IPagoSocioRepository
import com.google.api.core.ApiFuture
import com.google.api.core.ApiFutureCallback
import com.google.api.core.ApiFutures
import com.google.cloud.firestore.Firestore
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class PagoSocioRepository(private val firestore: Firestore) : IPagoSocioRepository {

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

    override suspend fun crearPagoSocio(pagoSocio: PagoSocio): PagoSocio {

        val socioDoc = pagoSocio.idSocio?.let { firestore.collection("socios").document(it).get().await() }
        if (socioDoc != null) {
            if (!socioDoc.exists()) {
                throw IllegalArgumentException("El socio con ID ${pagoSocio.idSocio} no existe")
            }
        }

        val docRef = firestore.collection("pagoSocio").document()
        val nuevaPagoSocio = pagoSocio.copy(idPagoSocio = docRef.id)
        docRef.set(nuevaPagoSocio).await()
        return nuevaPagoSocio
    }

    override suspend fun obtenerPagoSocioPorSocio(idSocio: String): List<PagoSocio> {

        val snapshot = firestore.collection("pagoSocio")
            .whereEqualTo("idSocio", idSocio)
            .get()
            .await()

        return snapshot.documents.mapNotNull { document ->
            document.toObject(PagoSocio::class.java).copy(idPagoSocio = document.id)
        }
    }

    override suspend fun obtenerPagoSocioPorId(idPagoSocio: String): PagoSocio? {

        val doc = firestore.collection("pagoSocio").document(idPagoSocio).get().await()
        return if (doc.exists()) {
            doc.toObject(PagoSocio::class.java)?.copy(idPagoSocio = doc.id)
        } else {
            null
        }
    }

    override suspend fun actualizarPagoSocio(idPagoSocio: String, pagoSocio: PagoSocio): Boolean {

        val docRef = firestore.collection("pagoSocio").document(idPagoSocio)
        docRef.set(pagoSocio).await()
        return true
    }

    override suspend fun eliminarPagoSocio(idPagoSocio: String): Boolean {

        firestore.collection("pagoSocio").document(idPagoSocio).delete().await()
        return true
    }


}