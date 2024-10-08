package com.example.repository.clases

import com.example.models.Inspeccion
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

        val docRef = firestore.collection("propuestaservicios").document()
        val nuevaInspeccion = inspeccion.copy(codInspeccion = docRef.id)
        docRef.set(nuevaInspeccion).await()
        return nuevaInspeccion
    }

    override suspend fun obtenerInspeccionPorPropuesta(idPropuesta: String): List<Inspeccion> {
        TODO("Not yet implemented")
    }

    override suspend fun obtenerInspeccionPorId(idInspeccion: String): Inspeccion? {
        TODO("Not yet implemented")
    }

    override suspend fun actualizarInspeccion(idInspeccion: String, inspeccion: Inspeccion): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun eliminarInspeccion(idInspeccion: String): Boolean {
        TODO("Not yet implemented")
    }


}