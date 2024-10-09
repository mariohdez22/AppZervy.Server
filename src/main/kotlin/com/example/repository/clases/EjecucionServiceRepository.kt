package com.example.repository.clases

import com.example.models.Direccion
import com.example.models.EjecucionServicio
import com.example.models.Inspeccion
import com.example.models.PropuestaServicio
import com.example.repository.interfaces.IEjecucionServicioRepository
import com.google.api.core.ApiFuture
import com.google.api.core.ApiFutureCallback
import com.google.api.core.ApiFutures
import com.google.cloud.firestore.Firestore
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class EjecucionServiceRepository(
    private val firestore: Firestore
) : IEjecucionServicioRepository {

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

    override suspend fun crearEjecucionServicio(ejServicio: EjecucionServicio): EjecucionServicio {
        val propuestaDoc = ejServicio.idPropuesta?.let {
            firestore.collection("propuestaservicios").document(it).get().await()
        }

        val socioDoc = ejServicio.idPropuesta?.let {
            firestore.collection("socios").document(it).get().await()
        }

//        if (propuestaDoc != null) {
//            if (!propuestaDoc.exists()) {
//                throw IllegalArgumentException(
//                    "La propuesta con ID ${ejServicio.idPropuesta} no existe"
//                )
//            }
//        }
//
//        if (socioDoc != null) {
//            if (!socioDoc.exists()) {
//                throw IllegalArgumentException(
//                    "El socio con ID ${ejServicio.idSocio} no existe"
//                )
//            }
//        }


        val docRef = firestore.collection("ejecucionservicio").document()
        val nuevoServicio = ejServicio.copy(codServicio = docRef.id)
        docRef.set(nuevoServicio).await()
        return nuevoServicio
    }

    override suspend fun obtenerEjecucionServicios(): List<EjecucionServicio> {
        val snapShot = firestore.collection("ejecucionservicio").get().await()
        return snapShot.documents.mapNotNull { document ->
            document.toObject(EjecucionServicio::class.java)
        }
    }

    override suspend fun obtenerEjecucionPorPropuesta(idPropuesta: String): List<EjecucionServicio> {
        val snapshot = firestore.collection("propuestaservicios")
            .whereEqualTo("idPropuesta", idPropuesta)
            .get()
            .await()

        return snapshot.documents.mapNotNull { document ->
            document.toObject(EjecucionServicio::class.java).copy(codServicio = document.id)
        }
    }

    override suspend fun obtenerEjecucionPorSocio(idSocio: String): List<EjecucionServicio> {
        val snapshot = firestore.collection("socios")
            .whereEqualTo("idSocio", idSocio)
            .get()
            .await()

        return snapshot.documents.mapNotNull { document ->
            document.toObject(EjecucionServicio::class.java).copy(codServicio = document.id)
        }
    }

    override suspend fun obtenerEjecucionPorId(codServicio: String): EjecucionServicio? {
        val doc = firestore.collection("ejecucionservicio").document(codServicio).get().await()
        return if (doc.exists()) {
            doc.toObject(EjecucionServicio::class.java)?.copy(codServicio = doc.id)
        } else {
            null
        }
    }

    override suspend fun actualizarEjecucion(codServicio: String, ejServicio: EjecucionServicio): Boolean {
        val docRef = firestore.collection("ejecucionservicio").document(codServicio)
        docRef.set(ejServicio).await()
        return true
    }

    override suspend fun eliminarEjecucion(codServicio: String): Boolean {
        firestore.collection("ejecucionservicio").document(codServicio).delete().await()
        return true
    }


}