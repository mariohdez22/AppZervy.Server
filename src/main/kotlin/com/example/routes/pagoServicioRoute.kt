package com.example.routes

import com.example.ApiResponse.ApiResponse
import com.example.DTOs.PagoServicioDTO
import com.example.Mappers.*
import com.example.repository.interfaces.IPagoServicioRepository
import com.google.api.core.ApiFuture
import com.google.api.core.ApiFutureCallback
import com.google.api.core.ApiFutures
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

fun Route.PagoServicioRouting(
    _repo: IPagoServicioRepository
){
    route(
        "/pagoServicio"
    ){

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

        post("/crearPago") {

            val apiResponse = ApiResponse<PagoServicioDTO>()

            try {

                val pagoDTO = call.receive<PagoServicioDTO>()
                val pago = pagoDTO.toPagoServicio()
                val nuevoPago = _repo.crearPago(pago)
                val responseDto = nuevoPago.toPagoServicioDTO()

                apiResponse.success = true
                apiResponse.message = "Pago creado exitosamente"
                apiResponse.data = responseDto

                call.respond(HttpStatusCode.Created, apiResponse)

            } catch (e: Exception){

                apiResponse.success = false
                apiResponse.message = "Error al crear pago"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }

        }

        get("/obtenerPagos") {

            val apiResponse = ApiResponse<List<PagoServicioDTO>>()

            try {

                val obtenerClientes = _repo.obtenerPagos()
                val responseDto = obtenerClientes.map { it.toPagoServicioDTO() }

                apiResponse.success = true
                apiResponse.message = "Pagos obtenidos exitosamente"
                apiResponse.data = responseDto

                call.respond(HttpStatusCode.OK , apiResponse)

            } catch (e: Exception){

                apiResponse.success = false
                apiResponse.message = "Error al obtener pagos"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }

        }

        get("/obtenerPagoPorCliente/{idCliente}") {

            val apiResponse = ApiResponse<List<PagoServicioDTO>>()
            val idCliente = call.parameters["idCliente"]

            if (idCliente != null) {

                try {

                    val pagos = _repo.obtenerPagoPorCliente(idCliente)
                    val responseDTOs = pagos.map { it.toPagoServicioDTO() }

                    apiResponse.success = true
                    apiResponse.message = "Pagos obtenidos exitosamente"
                    apiResponse.data = responseDTOs

                    call.respond(HttpStatusCode.OK, apiResponse)

                } catch (e: Exception) {

                    apiResponse.success = false
                    apiResponse.message = "Error al obtener los pagos"
                    apiResponse.errors = listOf(e.message ?: "Error desconocido")

                    call.respond(HttpStatusCode.InternalServerError, apiResponse)
                }

            } else {

                apiResponse.success = false
                apiResponse.message = "ID del cliente no fue proporcionado"
                apiResponse.errors = listOf("El parámetro 'idCliente' es obligatorio")

                call.respond(HttpStatusCode.BadRequest, apiResponse)
            }
        }

        get("/obtenerPagoPorSocio/{idSocio}") {

            val apiResponse = ApiResponse<List<PagoServicioDTO>>()
            val idSocio = call.parameters["idSocio"]

            if (idSocio != null) {

                try {

                    val pagos = _repo.obtenerPagoPorSocio(idSocio)
                    val responseDTOs = pagos.map { it.toPagoServicioDTO() }

                    apiResponse.success = true
                    apiResponse.message = "Pagos obtenidos exitosamente"
                    apiResponse.data = responseDTOs

                    call.respond(HttpStatusCode.OK, apiResponse)

                } catch (e: Exception) {

                    apiResponse.success = false
                    apiResponse.message = "Error al obtener los pagos"
                    apiResponse.errors = listOf(e.message ?: "Error desconocido")

                    call.respond(HttpStatusCode.InternalServerError, apiResponse)
                }

            } else {

                apiResponse.success = false
                apiResponse.message = "ID del socio no fue proporcionado"
                apiResponse.errors = listOf("El parámetro 'idSocio' es obligatorio")

                call.respond(HttpStatusCode.BadRequest, apiResponse)
            }
        }


        get("/obtenerPagoId/{idPago}") {

            val apiResponse = ApiResponse<PagoServicioDTO>()

            try {

                val id = call.parameters["idPago"]

                if (id != null) {

                    val pago = _repo.obtenerPagoPorId(id)

                    if (pago != null) {

                        val responseDto = pago.toPagoServicioDTO()

                        apiResponse.success = true
                        apiResponse.message = "Pago por id: $id"
                        apiResponse.data = responseDto

                        call.respond(HttpStatusCode.OK ,apiResponse)

                    } else {

                        call.respond(HttpStatusCode.NotFound, "Pago no encontrado")
                    }
                } else {
                    call.respond(HttpStatusCode.BadRequest, "ID del pago no proporcionado")
                }

            } catch (e: Exception){

                apiResponse.success = false
                apiResponse.message = "Error al obtener pago"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }

        }

        put("/actualizarPago") {

            val apiResponse = ApiResponse<Unit>()

            try {

                val pagoDto = call.receive<PagoServicioDTO>()
                val id = pagoDto.idPagoServicioDTO

                if (id != null) {

                    val pago = pagoDto.toPagoServicio()
                    val pagoEditado = _repo.actualizarPago(id, pago)

                    if (pagoEditado) {

                        apiResponse.success = true
                        apiResponse.message = "Pago actualizado"

                        call.respond(HttpStatusCode.OK, apiResponse)

                    } else {

                        apiResponse.success = false
                        apiResponse.message = "Pago no encontrado"
                        apiResponse.errors = listOf("No existe un pago con el ID proporcionado")

                        call.respond(HttpStatusCode.NotFound, apiResponse)
                    }

                } else {

                    apiResponse.success = false
                    apiResponse.message = "ID de pago no proporcionado"
                    apiResponse.errors = listOf("No se proporciono ningun id del pago")

                    call.respond(HttpStatusCode.BadRequest, apiResponse)
                }

            } catch (e: Exception){

                apiResponse.success = false
                apiResponse.message = "Error al actualizar el pago"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }
        }

        delete("/eliminarPago/{idPago}") {

            val apiResponse = ApiResponse<Unit>()
            val id = call.parameters["idPago"]

            if (id != null) {

                try {

                    val eliminado = _repo.eliminarPago(id)

                    if (eliminado) {

                        apiResponse.success = true
                        apiResponse.message = "Pago eliminado"

                        call.respond(HttpStatusCode.OK, apiResponse)

                    } else {

                        apiResponse.success = false
                        apiResponse.message = "Pago no encontrado"
                        apiResponse.errors = listOf("No existe un pago con el ID proporcionado")

                        call.respond(HttpStatusCode.NotFound, apiResponse)
                    }

                } catch (e: Exception){

                    apiResponse.success = false
                    apiResponse.message = "Error al eliminar el pago"
                    apiResponse.errors = listOf(e.message ?: "Error desconocido")

                    call.respond(HttpStatusCode.InternalServerError, apiResponse)
                }

            } else {

                apiResponse.success = false
                apiResponse.message = "ID del pago no proporcionado"
                apiResponse.errors = listOf("No se proporciono ningun id del pago")

                call.respond(HttpStatusCode.BadRequest, apiResponse)
            }
        }

    }
}