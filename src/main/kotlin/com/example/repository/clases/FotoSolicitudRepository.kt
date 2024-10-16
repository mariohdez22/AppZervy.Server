package com.example.repository.clases

import com.example.models.FotoSolicitud
import com.example.repository.interfaces.IFotoSolicitudRepository
import com.google.api.core.ApiFuture
import com.google.api.core.ApiFutureCallback
import com.google.api.core.ApiFutures
import com.google.cloud.firestore.Firestore
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FotoSolicitudRepository(private val firestore: Firestore) : IFotoSolicitudRepository {

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

    override suspend fun crearFotoSolicitud(fotoSolicitud: FotoSolicitud): FotoSolicitud {

        val solicitudDoc = fotoSolicitud.idSolicitud?.let { firestore.collection("solicitudServicio").document(it).get().await() }
        if (solicitudDoc != null) {
            if (!solicitudDoc.exists()) {
                throw IllegalArgumentException("El cliente con ID ${fotoSolicitud.idSolicitud} no existe")
            }
        }

        val docRef = firestore.collection("fotoSolicitud").document()
        val nuevaFotoSolicitud = fotoSolicitud.copy(idFotoSolicitud = docRef.id)
        docRef.set(nuevaFotoSolicitud).await()
        return nuevaFotoSolicitud
    }

    override suspend fun obtenerFotoSolicitudesPorSolicitud(idSolicitud: String): List<FotoSolicitud> {
        
        val snapshot = firestore.collection("fotoSolicitud")
            .whereEqualTo("idSolicitud", idSolicitud)
            .get()
            .await()

        return snapshot.documents.mapNotNull { document ->
            document.toObject(FotoSolicitud::class.java).copy(idFotoSolicitud = document.id)
        }
    }

    override suspend fun obtenerFotoSolicitudPorId(idFotoSolicitud: String): FotoSolicitud? {

        val doc = firestore.collection("fotoSolicitud").document(idFotoSolicitud).get().await()
        return if (doc.exists()) {
            doc.toObject(FotoSolicitud::class.java)?.copy(idFotoSolicitud = doc.id)
        } else {
            null
        }
    }

    override suspend fun actualizarFotoSolicitud(idFotoSolicitud: String, fotoSolicitud: FotoSolicitud): Boolean {

        val docRef = firestore.collection("fotoSolicitud").document(idFotoSolicitud)
        docRef.set(fotoSolicitud).await()
        return true
    }

    override suspend fun eliminarFotoSolicitud(idFotoSolicitud: String): Boolean {

        firestore.collection("fotoSolicitud").document(idFotoSolicitud).delete().await()
        return true
    }


}