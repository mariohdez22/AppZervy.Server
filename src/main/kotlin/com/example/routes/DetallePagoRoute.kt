package com.example.routes

import com.example.ApiResponse.ApiResponse
import com.example.DTOs.DetallePagoDTO
import com.example.Mappers.toDetalle
import com.example.Mappers.toDetalleDTO
import com.example.repository.interfaces.IDetallePagoRepository
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.DetallePagoRouting(
    _repo : IDetallePagoRepository
){
    route(
        "/detallePago"
    ){
        post("/crearDetalle") {

            val apiResponse = ApiResponse<DetallePagoDTO>()

            try {

                val detalleDTO = call.receive<DetallePagoDTO>()
                val detalle = detalleDTO.toDetalle()
                val nuevoDetalle = _repo.crearDetalle(detalle)
                val responseDTO = nuevoDetalle.toDetalleDTO()

                apiResponse.success = true
                apiResponse.message = "Detalle pago creado exitosamente"
                apiResponse.data = responseDTO

                call.respond(HttpStatusCode.Created, apiResponse)

            } catch (e: Exception) {

                apiResponse.success = false
                apiResponse.message = "Error al crear el detalle pago"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }

        }

        get("/obtenerDetallesPorServicio/{idServicio}") {

            val apiResponse = ApiResponse<List<DetallePagoDTO>>()
            val idServicio = call.parameters["idServicio"]

            if (idServicio != null) {

                try {

                    val servicios = _repo.obtenerDetallePorServicio(idServicio)
                    val responseDTOs = servicios.map { it.toDetalleDTO() }

                    apiResponse.success = true
                    apiResponse.message = "Detalles pagos obtenidos exitosamente"
                    apiResponse.data = responseDTOs

                    call.respond(HttpStatusCode.OK, apiResponse)

                } catch (e: Exception) {

                    apiResponse.success = false
                    apiResponse.message = "Error al obtener los Detalles pagos"
                    apiResponse.errors = listOf(e.message ?: "Error desconocido")

                    call.respond(HttpStatusCode.InternalServerError, apiResponse)
                }

            } else {

                apiResponse.success = false
                apiResponse.message = "ID del pago servicio no proporcionado"
                apiResponse.errors = listOf("El parámetro 'idServicio' es obligatorio")

                call.respond(HttpStatusCode.BadRequest, apiResponse)
            }
        }

        get("/obtenerDetallesPorInspeccion/{idInspeccion}") {

            val apiResponse = ApiResponse<List<DetallePagoDTO>>()
            val idInspeccion = call.parameters["idInspeccion"]

            if (idInspeccion != null) {

                try {

                    val inspeccion = _repo.obtenerDetallePorInspeccion(idInspeccion)
                    val responseDTOs = inspeccion.map { it.toDetalleDTO() }

                    apiResponse.success = true
                    apiResponse.message = "Detalles pagos obtenidos exitosamente"
                    apiResponse.data = responseDTOs

                    call.respond(HttpStatusCode.OK, apiResponse)

                } catch (e: Exception) {

                    apiResponse.success = false
                    apiResponse.message = "Error al obtener los Detalles pagos"
                    apiResponse.errors = listOf(e.message ?: "Error desconocido")

                    call.respond(HttpStatusCode.InternalServerError, apiResponse)
                }

            } else {

                apiResponse.success = false
                apiResponse.message = "ID de la inspeccion no proporcionado"
                apiResponse.errors = listOf("El parámetro 'idInspeccion' es obligatorio")

                call.respond(HttpStatusCode.BadRequest, apiResponse)
            }
        }

        get("/obtenerDetallePorId/{idDetalle}") {

            val apiResponse = ApiResponse<DetallePagoDTO>()
            val idDetalle = call.parameters["idDetalle"]

            if (idDetalle != null) {

                try {

                    val detalle = _repo.obtenerDetallePorId(idDetalle)
                    if (detalle != null) {
                        val responseDTO = detalle.toDetalleDTO()

                        apiResponse.success = true
                        apiResponse.message = "Detalle pago obtenido exitosamente"
                        apiResponse.data = responseDTO

                        call.respond(HttpStatusCode.OK, apiResponse)

                    } else {

                        apiResponse.success = false
                        apiResponse.message = "Detalle pago no encontrada"
                        apiResponse.errors =
                            listOf("No existe un detalle pago con el ID proporcionado")

                        call.respond(HttpStatusCode.NotFound, apiResponse)
                    }
                } catch (e: Exception) {

                    apiResponse.success = false
                    apiResponse.message = "Error al obtener el detalle pago"
                    apiResponse.errors = listOf(e.message ?: "Error desconocido")

                    call.respond(HttpStatusCode.InternalServerError, apiResponse)
                }

            } else {

                apiResponse.success = false
                apiResponse.message = "ID del detalle pago no proporcionado"
                apiResponse.errors = listOf("El parámetro 'idDetalle' es obligatorio")

                call.respond(HttpStatusCode.BadRequest, apiResponse)
            }
        }

        put("/actualizarDetalle") {

            val apiResponse = ApiResponse<Unit>()

            try {

                val detalleDto = call.receive<DetallePagoDTO>()

                val id = detalleDto.idDetalleDTO

                if (id != null) {

                    val detalle = detalleDto.toDetalle()

                    val detalleEditado = _repo.actualizarDetalle(id, detalle)

                    if (detalleEditado) {

                        apiResponse.success = true
                        apiResponse.message = "Detalle actualizada"

                        call.respond(HttpStatusCode.OK, apiResponse)

                    } else {

                        apiResponse.success = false
                        apiResponse.message = "Detalle no encontrada"
                        apiResponse.errors =
                            listOf("No existe un detalle con el ID proporcionado")

                        call.respond(HttpStatusCode.NotFound, apiResponse)
                    }

                } else {

                    apiResponse.success = false
                    apiResponse.message = "ID de detalle no proporcionado"
                    apiResponse.errors = listOf("No se proporciono ningun id de detalle")

                    call.respond(HttpStatusCode.BadRequest, apiResponse)
                }

            } catch (e: Exception){

                apiResponse.success = false
                apiResponse.message = "Error al actualizar el detalle pago"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }
        }

        delete("/eliminarDetalle/{idDetalle}") {

            val apiResponse = ApiResponse<Unit>()

            val id = call.parameters["idDetalle"]

            if (id != null) {

                try {

                    val eliminado = _repo.eliminarDetalle(id)

                    if (eliminado) {

                        apiResponse.success = true
                        apiResponse.message = "Detalle eliminado"

                        call.respond(HttpStatusCode.OK, apiResponse)

                    } else {

                        apiResponse.success = false
                        apiResponse.message = "Detalle pago no encontrado"
                        apiResponse.errors =
                            listOf("No existe un detalle pago con el ID proporcionado")

                        call.respond(HttpStatusCode.NotFound, apiResponse)
                    }

                } catch (e: Exception){

                    apiResponse.success = false
                    apiResponse.message = "Error al eliminar el detalle pago"
                    apiResponse.errors = listOf(e.message ?: "Error desconocido")

                    call.respond(HttpStatusCode.InternalServerError, apiResponse)
                }

            } else {

                apiResponse.success = false
                apiResponse.message = "ID detalle pago no proporcionado"
                apiResponse.errors = listOf("No se proporciono ningun id de direccion")

                call.respond(HttpStatusCode.BadRequest, apiResponse)
            }
        }


    }
}