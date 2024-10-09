package com.example.repository.clases

import com.example.models.Personal
import com.example.repository.interfaces.IPersonalRepository
import com.google.cloud.firestore.Firestore
import com.google.api.core.ApiFuture
import com.google.api.core.ApiFutureCallback
import com.google.api.core.ApiFutures
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException



class PersonalRepository(private val firestore: Firestore) : IPersonalRepository {

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

    override suspend fun crearPersonal(personal: Personal): Personal {

        val docRef = firestore.collection("personal").document()
        val nuevoPersonal = personal.copy(idPersonal = docRef.id)
        docRef.set(nuevoPersonal).await()
        return nuevoPersonal
    }

    override suspend fun obtenerPersonal(): List<Personal> {

        val snapshot = firestore.collection("personal").get().await()
        return snapshot.documents.mapNotNull { document ->
            document.toObject(Personal::class.java)?.copy(idPersonal = document.id)
        }
    }

    override suspend fun obtenerPersonalPorId(id: String): Personal? {

        val doc = firestore.collection("personal").document(id).get().await()
        return if (doc.exists()) {
            doc.toObject(Personal::class.java)?.copy(idPersonal = doc.id)
        } else {
            null
        }
    }

    override suspend fun actualizarPersonal(id: String, personal: Personal): Boolean {

        val docRef = firestore.collection("personal").document(id)
        val updatedPersonal = personal.copy(idPersonal = id)
        docRef.set(updatedPersonal).await()
        return true
    }

    override suspend fun eliminarPersonal(id: String): Boolean {

        firestore.collection("personal").document(id).delete().await()
        return true
    }

}