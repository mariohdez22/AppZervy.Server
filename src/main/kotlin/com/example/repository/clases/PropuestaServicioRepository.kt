package com.example.repository.clases

import com.example.models.PropuestaServicio
import com.example.repository.interfaces.IPropuestaServicioRepository
import com.google.api.core.ApiFuture
import com.google.api.core.ApiFutureCallback
import com.google.api.core.ApiFutures
import com.google.cloud.firestore.Firestore
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class PropuestaServicioRepository(
    private val firestore: Firestore
) : IPropuestaServicioRepository {

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

    override suspend fun crearPropuestaServicio(propuesta: PropuestaServicio): PropuestaServicio {
        val docRef = firestore.collection("propuestaservicios").document()
        val nuevaPropuesta = propuesta.copy(idPropuesta = docRef.id)
        docRef.set(nuevaPropuesta).await()
        return nuevaPropuesta
    }

    override suspend fun obtenerPropuestaServicio(): List<PropuestaServicio> {
        val snapShot = firestore.collection("propuestaservicios").get().await()
        return snapShot.documents.mapNotNull { document ->
            document.toObject(PropuestaServicio::class.java)
        }
    }

    override suspend fun obtenerPropuestaServicioPorId(id: String): PropuestaServicio? {
        val doc = firestore.collection("propuestaservicios").document(id).get().await()
        return if (doc.exists()){
            doc.toObject(PropuestaServicio::class.java)
        }else{
            null
        }
    }

    override suspend fun actualizarPropuestaServicio(id: String, propuesta: PropuestaServicio): Boolean {
        val docRef = firestore.collection("propuestaservicios").document(id)
        val updatePropuesta = propuesta.copy(idPropuesta = docRef.id)
        docRef.set(updatePropuesta).await()
        return true
    }

    override suspend fun eliminarPropuestaServicio(id: String): Boolean {
        firestore.collection("propuestaservicios").document(id).delete().await()
        return true
    }


}