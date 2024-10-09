package com.example.routes

import com.example.ApiResponse.ApiResponse
import com.example.DTOs.DireccionDTO
import com.example.DTOs.EjecucionServicioDTO
import com.example.Mappers.toDireccion
import com.example.Mappers.toDto
import com.example.Mappers.toEjecucionServicio
import com.example.Mappers.toEjecucionServicioDTO
import com.example.repository.interfaces.IEjecucionServicioRepository
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.EjecucionServicioRouting(
    _repo: IEjecucionServicioRepository
) {
    route("/ejecucionServicio") {

        post("/crearServicio") {

            val apiResponse = ApiResponse<EjecucionServicioDTO>()

            try {

                val ejServicioDTO = call.receive<EjecucionServicioDTO>()
                val ejServicio = ejServicioDTO.toEjecucionServicio()
                val nuevoServicio = _repo.crearEjecucionServicio(ejServicio)
                val responseDTO = nuevoServicio.toEjecucionServicioDTO()

                apiResponse.success = true
                apiResponse.message = "Servicio creado exitosamente"
                apiResponse.data = responseDTO

                call.respond(HttpStatusCode.Created, apiResponse)

            } catch (e: Exception) {

                apiResponse.success = false
                apiResponse.message = "Error al crear el servicio"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }

        }

        get("/obtenerServicioPorPropuesta/{idPropuesta}") {

            val apiResponse = ApiResponse<List<EjecucionServicioDTO>>()
            val idPropuesta = call.parameters["idPropuesta"]

            if (idPropuesta != null) {

                try {

                    val ejServicios = _repo.obtenerEjecucionPorPropuesta(idPropuesta)
                    val responseDTOs = ejServicios.map { it.toEjecucionServicioDTO() }

                    apiResponse.success = true
                    apiResponse.message = "Servicios obtenidos exitosamente"
                    apiResponse.data = responseDTOs

                    call.respond(HttpStatusCode.OK, apiResponse)

                } catch (e: Exception) {

                    apiResponse.success = false
                    apiResponse.message = "Error al obtener los servicios"
                    apiResponse.errors = listOf(e.message ?: "Error desconocido")

                    call.respond(HttpStatusCode.InternalServerError, apiResponse)
                }

            } else {

                apiResponse.success = false
                apiResponse.message = "ID de la propuesta no proporcionado"
                apiResponse.errors = listOf("El parámetro 'idPropuesta' es obligatorio")

                call.respond(HttpStatusCode.BadRequest, apiResponse)
            }
        }

        get("/obtenerServicioPorSocio/{idSocio}") {

            val apiResponse = ApiResponse<List<EjecucionServicioDTO>>()
            val idSocio = call.parameters["idSocio"]

            if (idSocio != null) {

                try {

                    val ejServicios = _repo.obtenerEjecucionPorSocio(idSocio)
                    val responseDTOs = ejServicios.map { it.toEjecucionServicioDTO() }

                    apiResponse.success = true
                    apiResponse.message = "Servicios obtenidos exitosamente"
                    apiResponse.data = responseDTOs

                    call.respond(HttpStatusCode.OK, apiResponse)

                } catch (e: Exception) {

                    apiResponse.success = false
                    apiResponse.message = "Error al obtener los servicios"
                    apiResponse.errors = listOf(e.message ?: "Error desconocido")

                    call.respond(HttpStatusCode.InternalServerError, apiResponse)
                }

            } else {

                apiResponse.success = false
                apiResponse.message = "ID del socio no proporcionado"
                apiResponse.errors = listOf("El parámetro 'idSocio' es obligatorio")

                call.respond(HttpStatusCode.BadRequest, apiResponse)
            }
        }

        get("/obtenerServicioPorCodigo/{codServicio}") {

            val apiResponse = ApiResponse<EjecucionServicioDTO>()
            val codServicio = call.parameters["codServicio"]

            if (codServicio != null) {

                try {

                    val ejServicio = _repo.obtenerEjecucionPorId(codServicio)
                    if (ejServicio != null) {
                        val responseDTO = ejServicio.toEjecucionServicioDTO()

                        apiResponse.success = true
                        apiResponse.message = "Servicio obtenido exitosamente"
                        apiResponse.data = responseDTO

                        call.respond(HttpStatusCode.OK, apiResponse)

                    } else {

                        apiResponse.success = false
                        apiResponse.message = "Servicio no encontrado"
                        apiResponse.errors =
                            listOf("No existe un codigo con el codigo proporcionado")

                        call.respond(HttpStatusCode.NotFound, apiResponse)
                    }
                } catch (e: Exception) {

                    apiResponse.success = false
                    apiResponse.message = "Error al obtener el servicio"
                    apiResponse.errors = listOf(e.message ?: "Error desconocido")

                    call.respond(HttpStatusCode.InternalServerError, apiResponse)
                }

            } else {

                apiResponse.success = false
                apiResponse.message = "Codigo del servicio no proporcionado"
                apiResponse.errors = listOf("El parámetro 'codServicio' es obligatorio")

                call.respond(HttpStatusCode.BadRequest, apiResponse)
            }
        }

        put("/actualizarServicio") {

            val apiResponse = ApiResponse<Unit>()

            try {

                val ejServicioDto = call.receive<EjecucionServicioDTO>()
                val cod = ejServicioDto.codServicioDTO

                if (cod != null) {

                    val ejServicio = ejServicioDto.toEjecucionServicio()
                    val servicioEditado = _repo.actualizarEjecucion(cod,ejServicio)

                    if (servicioEditado) {

                        apiResponse.success = true
                        apiResponse.message = "Servicio actualizado"

                        call.respond(HttpStatusCode.OK, apiResponse)

                    } else {

                        apiResponse.success = false
                        apiResponse.message = "Servicio no encontrado"
                        apiResponse.errors =
                            listOf("No existe un servicio con el codigo proporcionado")

                        call.respond(HttpStatusCode.NotFound, apiResponse)
                    }

                } else {

                    apiResponse.success = false
                    apiResponse.message = "Codigo de servicio no proporcionado"
                    apiResponse.errors = listOf("No se proporciono ningun codigo del servicio")

                    call.respond(HttpStatusCode.BadRequest, apiResponse)
                }

            } catch (e: Exception){

                apiResponse.success = false
                apiResponse.message = "Error al actualizar el servicio"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }
        }

        delete("/eliminarServicio/{codServicio}") {

            val apiResponse = ApiResponse<Unit>()

            val cod = call.parameters["codServicio"]

            if (cod != null) {

                try {

                    val eliminado = _repo.eliminarEjecucion(cod)

                    if (eliminado) {

                        apiResponse.success = true
                        apiResponse.message = "Servicio eliminado"

                        call.respond(HttpStatusCode.OK, apiResponse)

                    } else {

                        apiResponse.success = false
                        apiResponse.message = "Servicio no encontrado"
                        apiResponse.errors =
                            listOf("No existe un servicio con el codigo proporcionado")

                        call.respond(HttpStatusCode.NotFound, apiResponse)
                    }

                } catch (e: Exception){

                    apiResponse.success = false
                    apiResponse.message = "Error al eliminar el servicio"
                    apiResponse.errors = listOf(e.message ?: "Error desconocido")

                    call.respond(HttpStatusCode.InternalServerError, apiResponse)
                }

            } else {

                apiResponse.success = false
                apiResponse.message = "Codigo del servicio no proporcionado"
                apiResponse.errors = listOf("No se proporciono ningun codigo de servicio")

                call.respond(HttpStatusCode.BadRequest, apiResponse)
            }
        }

    }
}