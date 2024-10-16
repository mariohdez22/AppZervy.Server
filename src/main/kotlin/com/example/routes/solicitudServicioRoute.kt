package com.example.routes

import com.example.ApiResponse.ApiResponse
import com.example.DTOs.SolicitudServicioDTO
import com.example.Mappers.toDTO
import com.example.Mappers.toSolicitudServicio
import com.example.repository.interfaces.ISolicitudServicioRepository
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.solicitudServicioRouting(_repository : ISolicitudServicioRepository) {

    route("/solicitudServicio") {

        //--------------------------------------------------------------------------------------------------------------

        post("/crearSolicitudServicio") {

            val apiResponse = ApiResponse<SolicitudServicioDTO>()

            try {

                val solicitudServicioDTO = call.receive<SolicitudServicioDTO>()
                val solicitudServicio = solicitudServicioDTO.toSolicitudServicio()
                val nuevaSolicitudServicio = _repository.crearSolicitudServicio(solicitudServicio)
                val responseDTO = nuevaSolicitudServicio.toDTO()

                apiResponse.success = true
                apiResponse.message = "Solicitud creada exitosamente"
                apiResponse.data = responseDTO

                call.respond(HttpStatusCode.Created, apiResponse)

            } catch (e: Exception) {

                apiResponse.success = false
                apiResponse.message = "Error al crear la Solicitud"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }

        }

        //--------------------------------------------------------------------------------------------------------------

        get("/obtenerSolicitudServicioPorCliente/{idCliente}") {

            val apiResponse = ApiResponse<List<SolicitudServicioDTO>>()
            val idCliente = call.parameters["idCliente"]

            if (idCliente != null) {

                try {

                    val solicitudServicio = _repository.obtenerSolicitudServicioPorCliente(idCliente)
                    val responseDTOs = solicitudServicio.map { it.toDTO() }

                    apiResponse.success = true
                    apiResponse.message = "SolicitudServicios obtenidas exitosamente"
                    apiResponse.data = responseDTOs

                    call.respond(HttpStatusCode.OK, apiResponse)

                } catch (e: Exception) {

                    apiResponse.success = false
                    apiResponse.message = "Error al obtener las SolicitudServicios"
                    apiResponse.errors = listOf(e.message ?: "Error desconocido")

                    call.respond(HttpStatusCode.InternalServerError, apiResponse)
                }

            } else {

                apiResponse.success = false
                apiResponse.message = "ID de cliente no proporcionado"
                apiResponse.errors = listOf("El parámetro 'idCliente' es obligatorio")

                call.respond(HttpStatusCode.BadRequest, apiResponse)
            }
        }

        //--------------------------------------------------------------------------------------------------------------

        get("/obtenerSolicitudServicioPorId/{idSolicitudServicio}") {

            val apiResponse = ApiResponse<SolicitudServicioDTO>()
            val idSolicitud = call.parameters["idSolicitud"]

            if (idSolicitud != null) {

                try {

                    val solicitudServicio = _repository.obtenerSolicitudServicioPorId(idSolicitud)
                    if (solicitudServicio != null) {
                        val responseDTO = solicitudServicio.toDTO()

                        apiResponse.success = true
                        apiResponse.message = "Solicitud Servicio obtenida exitosamente"
                        apiResponse.data = responseDTO

                        call.respond(HttpStatusCode.OK, apiResponse)

                    } else {

                        apiResponse.success = false
                        apiResponse.message = "Solicitud Servicio no encontrada"
                        apiResponse.errors = listOf("No existe una Solicitud Servicio con el ID proporcionado")

                        call.respond(HttpStatusCode.NotFound, apiResponse)
                    }
                } catch (e: Exception) {

                    apiResponse.success = false
                    apiResponse.message = "Error al obtener la Solicitud Servicio"
                    apiResponse.errors = listOf(e.message ?: "Error desconocido")

                    call.respond(HttpStatusCode.InternalServerError, apiResponse)
                }

            } else {

                apiResponse.success = false
                apiResponse.message = "ID de Solicitud Servicio no proporcionado"
                apiResponse.errors = listOf("El parámetro 'idSolicitudServicio' es obligatorio")

                call.respond(HttpStatusCode.BadRequest, apiResponse)
            }
        }

        //--------------------------------------------------------------------------------------------------------------

        put("/actualizarSolicitudServicio") {

            val apiResponse = ApiResponse<Unit>()

            try {

                val solicitudServicioDto = call.receive<SolicitudServicioDTO>()

                val id = solicitudServicioDto.idSolicitud

                if (id != null) {

                    val solicitudServicio = solicitudServicioDto.toSolicitudServicio()

                    val solicitudServicioEditada = _repository.actualizarSolicitudServicio(id, solicitudServicio)

                    if (solicitudServicioEditada) {

                        apiResponse.success = true
                        apiResponse.message = "SolicitudServicio actualizada"

                        call.respond(HttpStatusCode.OK, apiResponse)

                    } else {

                        apiResponse.success = false
                        apiResponse.message = "SolicitudServicio no encontrada"
                        apiResponse.errors = listOf("No existe una SolicitudServicio con el ID proporcionado")

                        call.respond(HttpStatusCode.NotFound, apiResponse)
                    }

                } else {

                    apiResponse.success = false
                    apiResponse.message = "ID de SolicitudServicio no proporcionado"
                    apiResponse.errors = listOf("No se proporciono ningun id de Solicitud Servicio")

                    call.respond(HttpStatusCode.BadRequest, apiResponse)
                }

            } catch (e: Exception){

                apiResponse.success = false
                apiResponse.message = "Error al actualizar la Solicitud de Servicio"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }
        }

        //--------------------------------------------------------------------------------------------------------------

        delete("/eliminarSolicitudServicio/{id}") {

            val apiResponse = ApiResponse<Unit>()

            val id = call.parameters["id"]

            if (id != null) {

                try {

                    val eliminado = _repository.eliminarSolicitudServicio(id)

                    if (eliminado) {

                        apiResponse.success = true
                        apiResponse.message = "SolicitudServicio eliminada"

                        call.respond(HttpStatusCode.OK, apiResponse)

                    } else {

                        apiResponse.success = false
                        apiResponse.message = "SolicitudServicio no encontrada"
                        apiResponse.errors = listOf("No existe una SolicitudServicio con el ID proporcionado")

                        call.respond(HttpStatusCode.NotFound, apiResponse)
                    }

                } catch (e: Exception){

                    apiResponse.success = false
                    apiResponse.message = "Error al eliminar la SolicitudServicio"
                    apiResponse.errors = listOf(e.message ?: "Error desconocido")

                    call.respond(HttpStatusCode.InternalServerError, apiResponse)
                }

            } else {

                apiResponse.success = false
                apiResponse.message = "ID SolicitudServicio no proporcionado"
                apiResponse.errors = listOf("No se proporciono ningun id de SolicitudServicio")

                call.respond(HttpStatusCode.BadRequest, apiResponse)
            }
        }

    }
}