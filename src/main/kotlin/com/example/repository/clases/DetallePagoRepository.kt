package com.example.repository.clases

import com.example.models.DetallePago
import com.example.models.Direccion
import com.example.models.PagoServicio
import com.example.repository.interfaces.IDetallePagoRepository
import com.google.api.core.ApiFuture
import com.google.api.core.ApiFutureCallback
import com.google.api.core.ApiFutures
import com.google.cloud.firestore.Firestore
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class DetallePagoRepository(
    private val firestore: Firestore
) : IDetallePagoRepository {

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

    override suspend fun crearDetalle(detallePago: DetallePago): DetallePago {
        val clienteDoc = detallePago.idPagoServicio?.let {
            firestore.collection("pagoservicio").document(it).get().await()
        }

        val inspeccionDoc = detallePago.codInspeccion?.let {
            firestore.collection("inspeccion").document(it).get().await()
        }

        if (clienteDoc != null) {
            if (!clienteDoc.exists()) {
                throw IllegalArgumentException(
                    "El cliente con ID ${detallePago.idPagoServicio} no existe"
                )
            }
        }

        if (inspeccionDoc != null) {
            if (!inspeccionDoc.exists()) {
                throw IllegalArgumentException(
                    "El cliente con ID ${detallePago.codInspeccion} no existe"
                )
            }
        }

        val docRef = firestore.collection("detallepago").document()
        val nuevaDetalle = detallePago.copy(idDetalle = docRef.id)
        docRef.set(nuevaDetalle).await()
        return nuevaDetalle
    }

    override suspend fun obtenerDetalles(): List<DetallePago> {
        val snapShot = firestore.collection("detallepago").get().await()
        return snapShot.documents.mapNotNull { document ->
            document.toObject(DetallePago::class.java)
        }
    }

    override suspend fun obtenerDetallePorServicio(idPagoServicio: String): List<DetallePago> {
        val snapshot = firestore.collection("detallepago")
            .whereEqualTo("idPagoServicio", idPagoServicio)
            .get()
            .await()

        return snapshot.documents.mapNotNull { document ->
            document.toObject(DetallePago::class.java).copy(idDetalle = document.id)
        }
    }

    override suspend fun obtenerDetallePorInspeccion(codInspeccion: String): List<DetallePago> {
        val snapshot = firestore.collection("detallepago")
            .whereEqualTo("codInspeccion", codInspeccion)
            .get()
            .await()

        return snapshot.documents.mapNotNull { document ->
            document.toObject(DetallePago::class.java).copy(idDetalle = document.id)
        }
    }

    override suspend fun obtenerDetallePorId(idDetalle: String): DetallePago? {
        val doc = firestore.collection("detallepago").document(idDetalle).get().await()
        return if (doc.exists()) {
            doc.toObject(DetallePago::class.java)?.copy(idDetalle = doc.id)
        } else {
            null
        }
    }

    override suspend fun actualizarDetalle(idDetalle: String, detallePago: DetallePago): Boolean {
        val docRef = firestore.collection("detallepago").document(idDetalle)
        docRef.set(detallePago).await()
        return true
    }

    override suspend fun eliminarDetalle(idDetalle: String): Boolean {
        firestore.collection("detallepago").document(idDetalle).delete().await()
        return true
    }


}