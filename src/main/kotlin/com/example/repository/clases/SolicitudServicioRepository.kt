package com.example.repository.clases

import com.example.models.SolicitudServicio
import com.example.repository.interfaces.ISolicitudServicioRepository
import com.google.api.core.ApiFuture
import com.google.api.core.ApiFutureCallback
import com.google.api.core.ApiFutures
import com.google.cloud.firestore.Firestore
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class SolicitudServicioRepository(private val firestore: Firestore) : ISolicitudServicioRepository {

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

    override suspend fun crearSolicitudServicio(solicitudServicio: SolicitudServicio): SolicitudServicio {

        val clienteDoc = solicitudServicio.idCliente?.let { firestore.collection("clientes").document(it).get().await() }
        if (clienteDoc != null) {
            if (!clienteDoc.exists()) {
                throw IllegalArgumentException("El cliente con ID ${solicitudServicio.idCliente} no existe")
            }
        }

        val docRef = firestore.collection("solicitudServicio").document()
        val nuevaSolicitud = solicitudServicio.copy(idSolicitud = docRef.id)
        docRef.set(nuevaSolicitud).await()
        return nuevaSolicitud
    }

    override suspend fun obtenerSolicitudServicioPorCliente(idCliente: String): List<SolicitudServicio> {

        val snapshot = firestore.collection("solicitudServicio")
            .whereEqualTo("idCliente", idCliente)
            .get()
            .await()

        return snapshot.documents.mapNotNull { document ->
            document.toObject(SolicitudServicio::class.java).copy(idSolicitud = document.id)
        }
    }

    override suspend fun obtenerSolicitudServicioPorId(idSolicitudServicio: String): SolicitudServicio? {

        val doc = firestore.collection("solicitudServicio").document(idSolicitudServicio).get().await()
        return if (doc.exists()) {
            doc.toObject(SolicitudServicio::class.java)?.copy(idSolicitud = doc.id)
        } else {
            null
        }
    }

    override suspend fun actualizarSolicitudServicio(idSolicitudServicio: String, solicitudServicio: SolicitudServicio): Boolean {

        val docRef = firestore.collection("solicitudServicio").document(idSolicitudServicio)
        docRef.set(solicitudServicio).await()
        return true
    }

    override suspend fun eliminarSolicitudServicio(idSolicitudServicio: String): Boolean {

        firestore.collection("solicitudServicio").document(idSolicitudServicio).delete().await()
        return true
    }


}