package com.example.repository.clases

import com.example.models.Direccion
import com.example.models.Reseñas
import com.example.repository.interfaces.IReseñasRepository
import com.google.cloud.firestore.Firestore
import com.google.api.core.ApiFuture
import com.google.api.core.ApiFutureCallback
import com.google.api.core.ApiFutures
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException



class ReseñasRepository(private val firestore: Firestore) : IReseñasRepository {

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
    override suspend fun crearReseñas(reseñas: Reseñas): Reseñas {

        val docRef = firestore.collection("reseñas").document()
        val nuevoReseñas = reseñas.copy(idReseñas = docRef.id)
        docRef.set(nuevoReseñas).await()
        return nuevoReseñas
    }

    override suspend fun obtenerReseñasPorCliente(idCliente: String): List<Reseñas> {

        val snapshot = firestore.collection("reseñas")
            .whereEqualTo("idCliente", idCliente)
            .get()
            .await()

        return snapshot.documents.mapNotNull { document ->
            document.toObject(Reseñas::class.java).copy(idCliente = document.id)
        }
    }
    override suspend fun obtenerReseñasPorSocio(idSocio: String): List<Reseñas> {

        val snapshot = firestore.collection("reseñas")
            .whereEqualTo("idSocio", idSocio)
            .get()
            .await()

        return snapshot.documents.mapNotNull { document ->
            document.toObject(Reseñas::class.java).copy(idSocio = document.id)
        }
    }


    override suspend fun obtenerReseñasPorId(id: String): Reseñas? {

        val doc = firestore.collection("reseñas").document(id).get().await()
        return if (doc.exists()) {
            doc.toObject(Reseñas::class.java)?.copy(idReseñas = doc.id)
        } else {
            null
        }
    }

    override suspend fun actualizarReseñas(id: String, reseñas: Reseñas): Boolean {

        val docRef = firestore.collection("reseñas").document(id)
        val updatedReseñas = reseñas.copy(idReseñas = id)
        docRef.set(updatedReseñas).await()
        return true
    }

    override suspend fun eliminarReseñas(id: String): Boolean {

        firestore.collection("reseñas").document(id).delete().await()
        return true
    }

}