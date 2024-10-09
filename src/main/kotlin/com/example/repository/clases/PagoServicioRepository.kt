package com.example.repository.clases

import com.example.models.Direccion
import com.example.models.PagoServicio
import com.example.models.PropuestaServicio
import com.example.repository.interfaces.IPagoServicioRepository
import com.google.api.core.ApiFuture
import com.google.api.core.ApiFutureCallback
import com.google.api.core.ApiFutures
import com.google.cloud.firestore.Firestore
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class PagoServicioRepository(
    private val firestore: Firestore
) : IPagoServicioRepository {

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

    override suspend fun crearPago(pagoServicio: PagoServicio): PagoServicio {
        val clienteDoc = pagoServicio.idCliente?.let {
            firestore.collection("clientes").document(it).get().await()
        }
        val socioDoc = pagoServicio.idCliente?.let {
            firestore.collection("socios").document(it).get().await()
        }

        if (clienteDoc != null) {
            if (!clienteDoc.exists()) {
                throw IllegalArgumentException("El cliente con ID ${pagoServicio.idCliente} no existe")
            }
        }

        if (socioDoc != null) {
            if (!socioDoc.exists()) {
                throw IllegalArgumentException("El socio con ID ${pagoServicio.idSocio} no existe")
            }
        }

        val docRef = firestore.collection("pagoservicio").document()
        val nuevoPago = pagoServicio.copy(idPagoServicio = docRef.id)
        docRef.set(nuevoPago).await()
        return nuevoPago
    }

    override suspend fun obtenerPagos(): List<PagoServicio> {
        val snapShot = firestore.collection("pagoservicio").get().await()
        return snapShot.documents.mapNotNull { document ->
            document.toObject(PagoServicio::class.java)
        }
    }

    override suspend fun obtenerPagoPorCliente(idCliente: String): List<PagoServicio> {
        val snapshot = firestore.collection("clientes")
            .whereEqualTo("idCliente", idCliente)
            .get()
            .await()

        return snapshot.documents.mapNotNull { document ->
            document.toObject(PagoServicio::class.java).copy(idPagoServicio = document.id)
        }
    }

    override suspend fun obtenerPagoPorSocio(idSocio: String): List<PagoServicio> {
        val snapshot = firestore.collection("socios")
            .whereEqualTo("idSocio", idSocio)
            .get()
            .await()

        return snapshot.documents.mapNotNull { document ->
            document.toObject(PagoServicio::class.java).copy(idPagoServicio = document.id)
        }
    }

    override suspend fun obtenerPagoPorId(idPagoServicio: String): PagoServicio? {
        val doc = firestore.collection("pagoservicio").document(idPagoServicio).get().await()
        return if (doc.exists()) {
            doc.toObject(PagoServicio::class.java)?.copy(idPagoServicio = doc.id)
        } else {
            null
        }
    }

    override suspend fun actualizarPago(idPagoServicio: String, pagoServicio: PagoServicio): Boolean {
        val docRef = firestore.collection("pagoservicio").document(idPagoServicio)
        docRef.set(pagoServicio).await()
        return true
    }

    override suspend fun eliminarPago(idPagoServicio: String): Boolean {
        firestore.collection("pagoservicio").document(idPagoServicio).delete().await()
        return true
    }


}