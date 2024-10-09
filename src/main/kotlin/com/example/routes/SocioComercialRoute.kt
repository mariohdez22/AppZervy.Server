package com.example.routes

import com.example.ApiResponse.ApiResponse
import com.example.DTOs.SocioComercialDTO
import com.example.Mappers.toSocioComercial
import com.example.Mappers.toDto
import com.example.repository.interfaces.ISocioComercialRepository
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.socioComercialRoute(_repository: ISocioComercialRepository) {
    route("/sociosComerciales") {

        //--------------------------------------------------------------------------------------------------------------

        // Crear un nuevo socio comercial
        post("/crearSocioComercial") {

            val apiResponse = ApiResponse<SocioComercialDTO>()

            try {

                val socioComercialDto = call.receive<SocioComercialDTO>()
                val socioComercial = socioComercialDto.toSocioComercial()
                val nuevoSocioComercial = _repository.crearSocioComercial(socioComercial)
                val responseDto = nuevoSocioComercial.toDto()

                apiResponse.success = true
                apiResponse.message = "Socio comercial creado exitosamente"
                apiResponse.data = responseDto

                call.respond(HttpStatusCode.Created, apiResponse)

            } catch (e: Exception){

                apiResponse.success = false
                apiResponse.message = "Error al crear socio comercial"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }

        }

        //--------------------------------------------------------------------------------------------------------------

        // Obtener todos los socios comerciales
        get("/obtenerSociosComerciales") {

            val apiResponse = ApiResponse<List<SocioComercialDTO>>()

            try {

                val obtenerSociosComerciales = _repository.obtenerSociosComerciales()
                val responseDto = obtenerSociosComerciales.map { it.toDto() }

                apiResponse.success = true
                apiResponse.message = "Socios comerciales obtenidos exitosamente"
                apiResponse.data = responseDto

                call.respond(HttpStatusCode.OK , apiResponse)

            } catch (e: Exception){

                apiResponse.success = false
                apiResponse.message = "Error al obtener socios comerciales"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }

        }

        //--------------------------------------------------------------------------------------------------------------

        // Obtener un socio comercial por ID
        get("/obtenerSocioComercialId/{id}") {

            val apiResponse = ApiResponse<SocioComercialDTO>()

            try {

                val id = call.parameters["id"]

                if (id != null) {

                    val socioComercial = _repository.obtenerSocioComercialPorId(id)

                    if (socioComercial != null) {

                        val responseDto = socioComercial.toDto()

                        apiResponse.success = true
                        apiResponse.message = "Socio comercial por id: $id"
                        apiResponse.data = responseDto

                        call.respond(HttpStatusCode.OK ,apiResponse)

                    } else {

                        call.respond(HttpStatusCode.NotFound, "Socio comercial no encontrado")
                    }

                } else {

                    call.respond(HttpStatusCode.BadRequest, "ID de socio comercial no proporcionado")
                }

            } catch (e: Exception){

                apiResponse.success = false
                apiResponse.message = "Error al obtener socio comercial"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }

        }

        //--------------------------------------------------------------------------------------------------------------

        // Actualizar un socio comercial
        put("/actualizarSocioComercial") {

            val apiResponse = ApiResponse<Unit>()

            try {

                val socioComercialDto = call.receive<SocioComercialDTO>()

                val id = socioComercialDto.idSocioComercialDto

                if (id != null) {

                    val socioComercial = socioComercialDto.toSocioComercial()

                    val socioComercialEditado = _repository.actualizarSocioComercial(id, socioComercial)

                    if (socioComercialEditado) {

                        apiResponse.success = true
                        apiResponse.message = "Socio comercial actualizado"

                        call.respond(HttpStatusCode.OK, apiResponse)

                    } else {

                        apiResponse.success = false
                        apiResponse.message = "Socio comercial no encontrado"
                        apiResponse.errors = listOf("No existe un socio comercial con el ID proporcionado")

                        call.respond(HttpStatusCode.NotFound, apiResponse)
                    }

                } else {

                    apiResponse.success = false
                    apiResponse.message = "ID de socio comercial no proporcionado"
                    apiResponse.errors = listOf("No se proporcionó ningún ID de socio comercial")

                    call.respond(HttpStatusCode.BadRequest, apiResponse)
                }

            } catch (e: Exception){

                apiResponse.success = false
                apiResponse.message = "Error al actualizar el socio comercial"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }
        }

        //--------------------------------------------------------------------------------------------------------------

        // Eliminar un socio comercial
        delete("/eliminarSocioComercial/{id}") {

            val apiResponse = ApiResponse<Unit>()

            val id = call.parameters["id"]

            if (id != null) {

                try {

                    val eliminado = _repository.eliminarSocioComercial(id)

                    if (eliminado) {

                        apiResponse.success = true
                        apiResponse.message = "Socio comercial eliminado"

                        call.respond(HttpStatusCode.OK, apiResponse)

                    } else {

                        apiResponse.success = false
                        apiResponse.message = "Socio comercial no encontrado"
                        apiResponse.errors = listOf("No existe un socio comercial con el ID proporcionado")

                        call.respond(HttpStatusCode.NotFound, apiResponse)
                    }

                } catch (e: Exception){

                    apiResponse.success = false
                    apiResponse.message = "Error al eliminar el socio comercial"
                    apiResponse.errors = listOf(e.message ?: "Error desconocido")

                    call.respond(HttpStatusCode.InternalServerError, apiResponse)
                }

            } else {

                apiResponse.success = false
                apiResponse.message = "ID de socio comercial no proporcionado"
                apiResponse.errors = listOf("No se proporcionó ningún ID de socio comercial")

                call.respond(HttpStatusCode.BadRequest, apiResponse)
            }
        }

    }
}