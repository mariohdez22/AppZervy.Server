package com.example.routes

import com.example.ApiResponse.ApiResponse
import com.example.DTOs.EvidenciaServicioDTO
import com.example.Mappers.toDto
import com.example.Mappers.toEvidenciaServicio
import com.example.repository.interfaces.IEvidenciaServicioRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route


fun Route.evidenciaServicioRouting(_repository: IEvidenciaServicioRepository){

    route("/evidenciaServicio") {

        //--------------------------------------------------------------------------------------------------------------

        // Crear un nuevo evidenciaServicio
        post("/crearEvidenciaServicio") {

            val apiResponse = ApiResponse< EvidenciaServicioDTO>()

            try {

                val evidenciaServicioDTO = call.receive<EvidenciaServicioDTO>()
                val evidenciaServicio = evidenciaServicioDTO.toEvidenciaServicio()
                val nuevoEvidenciaServicio = _repository.crearEvidenciaServicio(evidenciaServicio)
                val responseDto = nuevoEvidenciaServicio.toDto()

                apiResponse.success = true
                apiResponse.message = "Evidencia Servicio creado exitosamente"
                apiResponse.data = responseDto

                call.respond(HttpStatusCode.Created, apiResponse)

            } catch (e: Exception){

                apiResponse.success = false
                apiResponse.message = "Error al crear Evidencia Servicio"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }

        }

        //--------------------------------------------------------------------------------------------------------------

        // Obtener Evidencia Servicio
        get("/obtenerEvidenciaServicio") {

            val apiResponse = ApiResponse<List<EvidenciaServicioDTO>>()

            try {

                val obtenerEvidenciaServicio = _repository.obtenerEvidenciaServicio()
                val responseDto = obtenerEvidenciaServicio.map { it.toDto() }

                apiResponse.success = true
                apiResponse.message = "Evidencia Servicio obtenido exitosamente"
                apiResponse.data = responseDto

                call.respond(HttpStatusCode.OK , apiResponse)

            } catch (e: Exception){

                apiResponse.success = false
                apiResponse.message = "Error al obtener Evidencia Servicio"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }

        }

        //--------------------------------------------------------------------------------------------------------------

        // Obtener un Evidencia Servicio por ID
        get("/obtenerEvidenciaServicioId/{id}") {

            val apiResponse = ApiResponse<EvidenciaServicioDTO>()

            try {

                val id = call.parameters["id"]

                if (id != null) {

                    val evidenciaServicio = _repository.obtenerEvidenciaServicioPorId(id)

                    if (evidenciaServicio != null) {

                        val responseDto = evidenciaServicio.toDto()

                        apiResponse.success = true
                        apiResponse.message = "Personal por id: $id"
                        apiResponse.data = responseDto

                        call.respond(HttpStatusCode.OK ,apiResponse)

                    } else {

                        call.respond(HttpStatusCode.NotFound, "Evidencia Servicio no encontrado")
                    }

                } else {

                    call.respond(HttpStatusCode.BadRequest, "ID de Evidencia Servicio no proporcionado")
                }

            } catch (e: Exception){

                apiResponse.success = false
                apiResponse.message = "Error al obtener Evidencia Servicio"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }

        }

        //--------------------------------------------------------------------------------------------------------------

        // Actualizar una Evidencia Servicio
        put("/actualizarEvidenciaServicio") {

            val apiResponse = ApiResponse<Unit>()

            try {

                val evidenciaServicioDTO = call.receive<EvidenciaServicioDTO>()

                val id = evidenciaServicioDTO.idEvidenciaServicio

                if (id != null) {

                    val evidenciaServicio = evidenciaServicioDTO.toEvidenciaServicio()

                    val evidenciaServicioEditado = _repository.actualizarEvidenciaServicio(id, evidenciaServicio)

                    if (evidenciaServicioEditado) {

                        apiResponse.success = true
                        apiResponse.message = "Evidencia Servicio actualizado"

                        call.respond(HttpStatusCode.OK, apiResponse)

                    } else {

                        apiResponse.success = false
                        apiResponse.message = "Evidencia Servicio no encontrado"
                        apiResponse.errors = listOf("No existe unaEvidencia Serviciocon el ID proporcionado")

                        call.respond(HttpStatusCode.NotFound, apiResponse)
                    }

                } else {

                    apiResponse.success = false
                    apiResponse.message = "ID de la Evidencia Servicio no proporcionada"
                    apiResponse.errors = listOf("No se a proporciono ningun id de Evidencia Servicio")

                    call.respond(HttpStatusCode.BadRequest, apiResponse)
                }

            } catch (e: Exception){

                apiResponse.success = false
                apiResponse.message = "Error al actualizar la Evidencia Servicio"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }
        }

        //--------------------------------------------------------------------------------------------------------------

        // Eliminar un Evidencia Servicio
        delete("/eliminarEvidenciaServicio/{id}") {

            val apiResponse = ApiResponse<Unit>()

            val id = call.parameters["id"]

            if (id != null) {

                try {

                    val eliminado = _repository.eliminarEvidenciaServicio(id)

                    if (eliminado) {

                        apiResponse.success = true
                        apiResponse.message = "Evidencia Servicio eliminado"

                        call.respond(HttpStatusCode.OK, apiResponse)

                    } else {

                        apiResponse.success = false
                        apiResponse.message = "Evidencia Servicio no encontrado"
                        apiResponse.errors = listOf("No existe una Evidencia Servicio con el ID proporcionado")

                        call.respond(HttpStatusCode.NotFound, apiResponse)
                    }

                } catch (e: Exception){

                    apiResponse.success = false
                    apiResponse.message = "Error al eliminar la Evidencia Servicio"
                    apiResponse.errors = listOf(e.message ?: "Error desconocido")

                    call.respond(HttpStatusCode.InternalServerError, apiResponse)
                }

            } else {

                apiResponse.success = false
                apiResponse.message = "ID Evidencia Servicio no proporcionado"
                apiResponse.errors = listOf("No se proporciono ningun id de Evidencia Servicio")

                call.respond(HttpStatusCode.BadRequest, apiResponse)
            }
        }

    }

}