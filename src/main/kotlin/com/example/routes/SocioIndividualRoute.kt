package com.example.routes

import com.example.ApiResponse.ApiResponse
import com.example.DTOs.SocioIndividualDTO
import com.example.Mappers.toSocioIndividual
import com.example.Mappers.toDto
import com.example.repository.interfaces.ISocioIndividualRepository
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.socioIndividualRouting(_repository: ISocioIndividualRepository) {
    route("/sociosIndividuales") {

        //--------------------------------------------------------------------------------------------------------------

        // Crear un nuevo socio individual
        post("/crearSocioIndividual") {

            val apiResponse = ApiResponse<SocioIndividualDTO>()

            try {

                val socioIndividualDto = call.receive<SocioIndividualDTO>()
                val socioIndividual = socioIndividualDto.toSocioIndividual()
                val nuevoSocioIndividual = _repository.crearSocioIndividual(socioIndividual)
                val responseDto = nuevoSocioIndividual.toDto()

                apiResponse.success = true
                apiResponse.message = "Socio individual creado exitosamente"
                apiResponse.data = responseDto

                call.respond(HttpStatusCode.Created, apiResponse)

            } catch (e: Exception){

                apiResponse.success = false
                apiResponse.message = "Error al crear socio individual"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }

        }

        //--------------------------------------------------------------------------------------------------------------

        // Obtener todos los socios individuales
        get("/obtenerSociosIndividuales") {

            val apiResponse = ApiResponse<List<SocioIndividualDTO>>()

            try {

                val obtenerSociosIndividuales = _repository.obtenerSociosIndividuales()
                val responseDto = obtenerSociosIndividuales.map { it.toDto() }

                apiResponse.success = true
                apiResponse.message = "Socios individuales obtenidos exitosamente"
                apiResponse.data = responseDto

                call.respond(HttpStatusCode.OK , apiResponse)

            } catch (e: Exception){

                apiResponse.success = false
                apiResponse.message = "Error al obtener socios individuales"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }

        }

        //--------------------------------------------------------------------------------------------------------------

        // Obtener un socio individual por ID
        get("/obtenerSocioIndividualId/{id}") {

            val apiResponse = ApiResponse<SocioIndividualDTO>()

            try {

                val id = call.parameters["id"]

                if (id != null) {

                    val socioIndividual = _repository.obtenerSocioIndividualPorId(id)

                    if (socioIndividual != null) {

                        val responseDto = socioIndividual.toDto()

                        apiResponse.success = true
                        apiResponse.message = "Socio individual por id: $id"
                        apiResponse.data = responseDto

                        call.respond(HttpStatusCode.OK ,apiResponse)

                    } else {

                        call.respond(HttpStatusCode.NotFound, "Socio individual no encontrado")
                    }

                } else {

                    call.respond(HttpStatusCode.BadRequest, "ID de socio individual no proporcionado")
                }

            } catch (e: Exception){

                apiResponse.success = false
                apiResponse.message = "Error al obtener socio individual"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }

        }

        //--------------------------------------------------------------------------------------------------------------

        // Actualizar un socio individual
        put("/actualizarSocioIndividual") {

            val apiResponse = ApiResponse<Unit>()

            try {

                val socioIndividualDto = call.receive<SocioIndividualDTO>()

                val id = socioIndividualDto.idSocioIndividualDto

                if (id != null) {

                    val socioIndividual = socioIndividualDto.toSocioIndividual()

                    val socioIndividualEditado = _repository.actualizarSocioIndividual(id, socioIndividual)

                    if (socioIndividualEditado) {

                        apiResponse.success = true
                        apiResponse.message = "Socio individual actualizado"

                        call.respond(HttpStatusCode.OK, apiResponse)

                    } else {

                        apiResponse.success = false
                        apiResponse.message = "Socio individual no encontrado"
                        apiResponse.errors = listOf("No existe un socio individual con el ID proporcionado")

                        call.respond(HttpStatusCode.NotFound, apiResponse)
                    }

                } else {

                    apiResponse.success = false
                    apiResponse.message = "ID de socio individual no proporcionado"
                    apiResponse.errors = listOf("No se proporcionó ningún ID de socio individual")

                    call.respond(HttpStatusCode.BadRequest, apiResponse)
                }

            } catch (e: Exception){

                apiResponse.success = false
                apiResponse.message = "Error al actualizar el socio individual"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }
        }

        //--------------------------------------------------------------------------------------------------------------

        // Eliminar un socio individual
        delete("/eliminarSocioIndividual/{id}") {

            val apiResponse = ApiResponse<Unit>()

            val id = call.parameters["id"]

            if (id != null) {

                try {

                    val eliminado = _repository.eliminarSocioIndividual(id)

                    if (eliminado) {

                        apiResponse.success = true
                        apiResponse.message = "Socio individual eliminado"

                        call.respond(HttpStatusCode.OK, apiResponse)

                    } else {

                        apiResponse.success = false
                        apiResponse.message = "Socio individual no encontrado"
                        apiResponse.errors = listOf("No existe un socio individual con el ID proporcionado")

                        call.respond(HttpStatusCode.NotFound, apiResponse)
                    }

                } catch (e: Exception){

                    apiResponse.success = false
                    apiResponse.message = "Error al eliminar el socio individual"
                    apiResponse.errors = listOf(e.message ?: "Error desconocido")

                    call.respond(HttpStatusCode.InternalServerError, apiResponse)
                }

            } else {

                apiResponse.success = false
                apiResponse.message = "ID de socio individual no proporcionado"
                apiResponse.errors = listOf("No se proporcionó ningún ID de socio individual")

                call.respond(HttpStatusCode.BadRequest, apiResponse)
            }
        }

    }
}