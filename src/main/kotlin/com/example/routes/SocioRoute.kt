package com.example.routes

import com.example.ApiResponse.ApiResponse
import com.example.DTOs.SocioDTO
import com.example.Mappers.toSocio
import com.example.Mappers.toDto
import com.example.repository.interfaces.ISocioRepository
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.socioRouting(_repository: ISocioRepository) {
    route("/socios") {

        //--------------------------------------------------------------------------------------------------------------

        // Crear un nuevo socio
        post("/crearSocio") {

            val apiResponse = ApiResponse<SocioDTO>()

            try {

                val socioDto = call.receive<SocioDTO>()
                val socio = socioDto.toSocio()
                val nuevoSocio = _repository.crearSocio(socio)
                val responseDto = nuevoSocio.toDto()

                apiResponse.success = true
                apiResponse.message = "Socio creado exitosamente"
                apiResponse.data = responseDto

                call.respond(HttpStatusCode.Created, apiResponse)

            } catch (e: Exception){

                apiResponse.success = false
                apiResponse.message = "Error al crear socio"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }

        }

        //--------------------------------------------------------------------------------------------------------------

        // Obtener todos los socios
        get("/obtenerSocios") {

            val apiResponse = ApiResponse<List<SocioDTO>>()

            try {

                val obtenerSocios = _repository.obtenerSocios()
                val responseDto = obtenerSocios.map { it.toDto() }

                apiResponse.success = true
                apiResponse.message = "Socios obtenidos exitosamente"
                apiResponse.data = responseDto

                call.respond(HttpStatusCode.OK , apiResponse)

            } catch (e: Exception){

                apiResponse.success = false
                apiResponse.message = "Error al obtener socios"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }

        }

        //--------------------------------------------------------------------------------------------------------------

        // Obtener un socio por ID
        get("/obtenerSocioId/{id}") {

            val apiResponse = ApiResponse<SocioDTO>()

            try {

                val id = call.parameters["id"]

                if (id != null) {

                    val socio = _repository.obtenerSocioPorId(id)

                    if (socio != null) {

                        val responseDto = socio.toDto()

                        apiResponse.success = true
                        apiResponse.message = "Socio por id: $id"
                        apiResponse.data = responseDto

                        call.respond(HttpStatusCode.OK ,apiResponse)

                    } else {

                        call.respond(HttpStatusCode.NotFound, "Socio no encontrado")
                    }

                } else {

                    call.respond(HttpStatusCode.BadRequest, "ID de socio no proporcionado")
                }

            } catch (e: Exception){

                apiResponse.success = false
                apiResponse.message = "Error al obtener socio"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }

        }

        //--------------------------------------------------------------------------------------------------------------

        // Actualizar un socio
        put("/actualizarSocio") {

            val apiResponse = ApiResponse<Unit>()

            try {

                val socioDto = call.receive<SocioDTO>()

                val id = socioDto.idSocioDto

                if (id != null) {

                    val socio = socioDto.toSocio()

                    val socioEditado = _repository.actualizarSocio(id, socio)

                    if (socioEditado) {

                        apiResponse.success = true
                        apiResponse.message = "Socio actualizado"

                        call.respond(HttpStatusCode.OK, apiResponse)

                    } else {

                        apiResponse.success = false
                        apiResponse.message = "Socio no encontrado"
                        apiResponse.errors = listOf("No existe un socio con el ID proporcionado")

                        call.respond(HttpStatusCode.NotFound, apiResponse)
                    }

                } else {

                    apiResponse.success = false
                    apiResponse.message = "ID de socio no proporcionado"
                    apiResponse.errors = listOf("No se proporcionó ningún ID de socio")

                    call.respond(HttpStatusCode.BadRequest, apiResponse)
                }

            } catch (e: Exception){

                apiResponse.success = false
                apiResponse.message = "Error al actualizar el socio"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }
        }

        //--------------------------------------------------------------------------------------------------------------

        // Eliminar un socio
        delete("/eliminarSocio/{id}") {

            val apiResponse = ApiResponse<Unit>()

            val id = call.parameters["id"]

            if (id != null) {

                try {

                    val eliminado = _repository.eliminarSocio(id)

                    if (eliminado) {

                        apiResponse.success = true
                        apiResponse.message = "Socio eliminado"

                        call.respond(HttpStatusCode.OK, apiResponse)

                    } else {

                        apiResponse.success = false
                        apiResponse.message = "Socio no encontrado"
                        apiResponse.errors = listOf("No existe un socio con el ID proporcionado")

                        call.respond(HttpStatusCode.NotFound, apiResponse)
                    }

                } catch (e: Exception){

                    apiResponse.success = false
                    apiResponse.message = "Error al eliminar el socio"
                    apiResponse.errors = listOf(e.message ?: "Error desconocido")

                    call.respond(HttpStatusCode.InternalServerError, apiResponse)
                }

            } else {

                apiResponse.success = false
                apiResponse.message = "ID de socio no proporcionado"
                apiResponse.errors = listOf("No se proporcionó ningún ID de socio")

                call.respond(HttpStatusCode.BadRequest, apiResponse)
            }
        }

    }
}