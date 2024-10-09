package com.example.repository.clases

import com.example.models.Direccion
import com.example.models.Inspeccion
import com.example.models.PropuestaServicio
import com.example.repository.interfaces.IInspeccionRepository
import com.google.api.core.ApiFuture
import com.google.api.core.ApiFutureCallback
import com.google.api.core.ApiFutures
import com.google.cloud.firestore.DocumentSnapshot
import com.google.cloud.firestore.Firestore
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class InspeccionRepository(
    private val firestore: Firestore
) : IInspeccionRepository {

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

    override suspend fun crearInspeccion(inspeccion: Inspeccion): Inspeccion {
        val propuestaDoc = inspeccion.idPropuesta?.let{
            firestore.collection("propuestaservicios").document(it).get().await()
        }
        if(propuestaDoc != null) {
            if(!propuestaDoc.exists()) {
                throw IllegalArgumentException(
                    "La propuesta con ID ${inspeccion.idPropuesta} no existe"
                )
            }
        }

        val docRef = firestore.collection("inspeccion").document()
        val nuevaInspeccion = inspeccion.copy(codInspeccion = docRef.id)
        docRef.set(nuevaInspeccion).await()
        return nuevaInspeccion
    }

    override suspend fun obtenerInspecciones(): List<Inspeccion> {
        val snapShot = firestore.collection("inspeccion").get().await()
        return snapShot.documents.mapNotNull { document ->
            document.toObject(Inspeccion::class.java)
        }
    }

    override suspend fun obtenerInspeccionPorPropuesta(idPropuesta: String): List<Inspeccion> {
        val snapshot = firestore.collection("propuestaservicios")
            .whereEqualTo("idPropuesta", idPropuesta)
            .get()
            .await()

        return snapshot.documents.mapNotNull { document ->
            document.toObject(Inspeccion::class.java).copy(codInspeccion = document.id)
        }
    }

    override suspend fun obtenerInspeccionPorId(codInspeccion: String): Inspeccion? {
        val doc = firestore.collection("inspeccion").document(codInspeccion).get().await()
        return if (doc.exists()) {
            doc.toObject(Inspeccion::class.java)?.copy(codInspeccion = doc.id)
        } else {
            null
        }
    }

    override suspend fun actualizarInspeccion(codInspeccion: String, inspeccion: Inspeccion): Boolean {
        val docRef = firestore.collection("inspeccion").document(codInspeccion)
        docRef.set(inspeccion).await()
        return true
    }

    override suspend fun eliminarInspeccion(codInspeccion: String): Boolean {
        firestore.collection("inspeccion").document(codInspeccion).delete().await()
        return true
    }


}