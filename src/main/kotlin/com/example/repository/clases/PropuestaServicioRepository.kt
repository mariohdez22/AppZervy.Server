package com.example.repository.clases

import com.example.models.Direccion
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
        val socioDoc = propuesta.idSocio?.let {
            firestore.collection("socios").document(it).get().await()
        }

        val solicitudDoc = propuesta.idSolicitud?.let {
            firestore.collection("solicitudServicio").document(it).get().await()
        }

        if (socioDoc != null) {
            if (!socioDoc.exists()) {
                throw IllegalArgumentException(
                    "El socio con ID ${propuesta.idSocio} no existe"
                )
            }
        }

        if (solicitudDoc != null) {
            if (!solicitudDoc.exists()) {
                throw IllegalArgumentException(
                    "La solicitud con ID ${propuesta.idSolicitud} no existe"
                )
            }
        }

        val docRef = firestore.collection("propuestaServicio").document()
        val nuevaPropuesta = propuesta.copy(idPropuesta = docRef.id)
        docRef.set(nuevaPropuesta).await()
        return nuevaPropuesta
    }

    override suspend fun obtenerPropuestaServicio(): List<PropuestaServicio> {
        val snapShot = firestore.collection("propuestaServicio").get().await()
        return snapShot.documents.mapNotNull { document ->
            document.toObject(PropuestaServicio::class.java)
        }
    }

    override suspend fun obtenerPropuestaServicioPorSocio(idSocio: String): List<PropuestaServicio> {
        val snapshot = firestore.collection("propuestaServicio")
            .whereEqualTo("idSocio", idSocio)
            .get()
            .await()

        return snapshot.documents.mapNotNull { document ->
            document.toObject(PropuestaServicio::class.java).copy(idPropuesta = document.id)
        }
    }

    override suspend fun obtenerPropuestaServicioPorSolicitud(idSolicitud: String): List<PropuestaServicio> {
        val snapshot = firestore.collection("propuestaServicio")
            .whereEqualTo("idSolicitud", idSolicitud)
            .get()
            .await()

        return snapshot.documents.mapNotNull { document ->
            document.toObject(PropuestaServicio::class.java).copy(idPropuesta = document.id)
        }
    }

    override suspend fun obtenerPropuestaServicioPorId(id: String): PropuestaServicio? {
        val doc = firestore.collection("propuestaServicio").document(id).get().await()
        return if (doc.exists()){
            doc.toObject(PropuestaServicio::class.java)
        }else{
            null
        }
    }

    override suspend fun actualizarPropuestaServicio(id: String, propuesta: PropuestaServicio): Boolean {
        val docRef = firestore.collection("propuestaServicio").document(id)
        val updatePropuesta = propuesta.copy(idPropuesta = docRef.id)
        docRef.set(updatePropuesta).await()
        return true
    }

    override suspend fun eliminarPropuestaServicio(id: String): Boolean {
        firestore.collection("propuestaServicio").document(id).delete().await()
        return true
    }


}