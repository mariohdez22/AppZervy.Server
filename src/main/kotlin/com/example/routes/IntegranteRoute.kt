package com.example.routes

import com.example.ApiResponse.ApiResponse
import com.example.DTOs.IntegrantesDTO
import com.example.Mappers.toIntegrante
import com.example.Mappers.toDto
import com.example.repository.interfaces.IIntegranteRepository
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.integranteRouting(_repository: IIntegranteRepository) {
    route("/integrantes") {

        //--------------------------------------------------------------------------------------------------------------

        // Crear un nuevo integrante
        post("/crearIntegrante") {

            val apiResponse = ApiResponse<IntegrantesDTO>()

            try {

                val integranteDto = call.receive<IntegrantesDTO>()
                val integrante = integranteDto.toIntegrante()
                val nuevoIntegrante = _repository.crearIntegrante(integrante)
                val responseDto = nuevoIntegrante.toDto()

                apiResponse.success = true
                apiResponse.message = "Integrante creado exitosamente"
                apiResponse.data = responseDto

                call.respond(HttpStatusCode.Created, apiResponse)

            } catch (e: Exception){

                apiResponse.success = false
                apiResponse.message = "Error al crear integrante"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }

        }

        //--------------------------------------------------------------------------------------------------------------

        // Obtener todos los integrantes
        get("/obtenerIntegrantes") {

            val apiResponse = ApiResponse<List<IntegrantesDTO>>()

            try {

                val obtenerIntegrantes = _repository.obtenerIntegrantes()
                val responseDto = obtenerIntegrantes.map { it.toDto() }

                apiResponse.success = true
                apiResponse.message = "Integrantes obtenidos exitosamente"
                apiResponse.data = responseDto

                call.respond(HttpStatusCode.OK , apiResponse)

            } catch (e: Exception){

                apiResponse.success = false
                apiResponse.message = "Error al obtener integrantes"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }

        }

        //--------------------------------------------------------------------------------------------------------------

        // Obtener un integrante por ID
        get("/obtenerIntegranteId/{id}") {

            val apiResponse = ApiResponse<IntegrantesDTO>()

            try {

                val id = call.parameters["id"]

                if (id != null) {

                    val integrante = _repository.obtenerIntegrantePorId(id)

                    if (integrante != null) {

                        val responseDto = integrante.toDto()

                        apiResponse.success = true
                        apiResponse.message = "Integrante por id: $id"
                        apiResponse.data = responseDto

                        call.respond(HttpStatusCode.OK ,apiResponse)

                    } else {

                        call.respond(HttpStatusCode.NotFound, "Integrante no encontrado")
                    }

                } else {

                    call.respond(HttpStatusCode.BadRequest, "ID de integrante no proporcionado")
                }

            } catch (e: Exception){

                apiResponse.success = false
                apiResponse.message = "Error al obtener integrante"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }

        }

        //--------------------------------------------------------------------------------------------------------------

        // Actualizar un integrante
        put("/actualizarIntegrante") {

            val apiResponse = ApiResponse<Unit>()

            try {

                val integranteDto = call.receive<IntegrantesDTO>()

                val id = integranteDto.idIntegranteDto

                if (id != null) {

                    val integrante = integranteDto.toIntegrante()

                    val integranteEditado = _repository.actualizarIntegrante(id, integrante)

                    if (integranteEditado) {

                        apiResponse.success = true
                        apiResponse.message = "Integrante actualizado"

                        call.respond(HttpStatusCode.OK, apiResponse)

                    } else {

                        apiResponse.success = false
                        apiResponse.message = "Integrante no encontrado"
                        apiResponse.errors = listOf("No existe un integrante con el ID proporcionado")

                        call.respond(HttpStatusCode.NotFound, apiResponse)
                    }

                } else {

                    apiResponse.success = false
                    apiResponse.message = "ID de integrante no proporcionado"
                    apiResponse.errors = listOf("No se proporciono ningun id de integrante")

                    call.respond(HttpStatusCode.BadRequest, apiResponse)
                }

            } catch (e: Exception){

                apiResponse.success = false
                apiResponse.message = "Error al actualizar el integrante"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }
        }

        //--------------------------------------------------------------------------------------------------------------

        // Eliminar un integrante
        delete("/eliminarIntegrante/{id}") {

            val apiResponse = ApiResponse<Unit>()

            val id = call.parameters["id"]

            if (id != null) {

                try {

                    val eliminado = _repository.eliminarIntegrante(id)

                    if (eliminado) {

                        apiResponse.success = true
                        apiResponse.message = "Integrante eliminado"

                        call.respond(HttpStatusCode.OK, apiResponse)

                    } else {

                        apiResponse.success = false
                        apiResponse.message = "Integrante no encontrado"
                        apiResponse.errors = listOf("No existe un integrante con el ID proporcionado")

                        call.respond(HttpStatusCode.NotFound, apiResponse)
                    }

                } catch (e: Exception){

                    apiResponse.success = false
                    apiResponse.message = "Error al eliminar el integrante"
                    apiResponse.errors = listOf(e.message ?: "Error desconocido")

                    call.respond(HttpStatusCode.InternalServerError, apiResponse)
                }

            } else {

                apiResponse.success = false
                apiResponse.message = "ID integrante no proporcionado"
                apiResponse.errors = listOf("No se proporciono ningun id de integrante")

                call.respond(HttpStatusCode.BadRequest, apiResponse)
            }
        }

    }
}