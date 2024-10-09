package com.example.repository.clases

import com.example.models.CategoriaServicio
import com.example.models.Cliente
import com.example.repository.interfaces.ICategoriaServicioRepository
import com.google.api.core.ApiFuture
import com.google.api.core.ApiFutureCallback
import com.google.api.core.ApiFutures
import com.google.cloud.firestore.Firestore
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class CategoriaServicioRepository(private val firestore: Firestore) : ICategoriaServicioRepository {

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

    override suspend fun crearCategoriaServicio(categoriaServicio: CategoriaServicio): CategoriaServicio {

        val docRef = firestore.collection("categoriaservicio").document()
        val nuevoCategoria = categoriaServicio.copy(idCategoriaServicio = docRef.id)
        docRef.set(nuevoCategoria).await()
        return nuevoCategoria
    }

    override suspend fun obtenerCategoriaServicio(): List<CategoriaServicio> {

        val snapshot = firestore.collection("categoriaservicio").get().await()
        return snapshot.documents.mapNotNull { document ->
            document.toObject(CategoriaServicio::class.java)?.copy(idCategoriaServicio = document.id)
        }
    }

    override suspend fun obtenerCategoriaServicioPorId(id: String): CategoriaServicio? {

        val doc = firestore.collection("categoriaservicio").document(id).get().await()
        return if (doc.exists()) {
            doc.toObject(CategoriaServicio::class.java)?.copy(idCategoriaServicio = doc.id)
        } else {
            null
        }
    }

    override suspend fun actualizarCategoriaServicio(id: String, categoriaServicio: CategoriaServicio): Boolean {

        val docRef = firestore.collection("categoriaservicio").document(id)
        val updatedCategoria = categoriaServicio.copy(idCategoriaServicio = id)
        docRef.set(updatedCategoria).await()
        return true
    }

    override suspend fun eliminarCategoriaServicio(id: String): Boolean {

        firestore.collection("categoriaservicio").document(id).delete().await()
        return true
    }


}