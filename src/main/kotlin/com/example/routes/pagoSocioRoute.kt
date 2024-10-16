package com.example.routes

import com.example.ApiResponse.ApiResponse
import com.example.DTOs.PagoSocioDTO
import com.example.Mappers.toDto
import com.example.Mappers.toPagoSocio
import com.example.repository.interfaces.IPagoSocioRepository
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.pagoSocioRouting(_repository : IPagoSocioRepository) {

    route("/pagoSocio") {

        //--------------------------------------------------------------------------------------------------------------

        post("/crearPagoSocio") {

            val apiResponse = ApiResponse<PagoSocioDTO>()

            try {

                val pagoSocioDTO = call.receive<PagoSocioDTO>()
                val pagoSocio = pagoSocioDTO.toPagoSocio()
                val nuevaPagoSocio = _repository.crearPagoSocio(pagoSocio)
                val responseDTO = nuevaPagoSocio.toDto()

                apiResponse.success = true
                apiResponse.message = "PagoSocio creada exitosamente"
                apiResponse.data = responseDTO

                call.respond(HttpStatusCode.Created, apiResponse)

            } catch (e: Exception) {

                apiResponse.success = false
                apiResponse.message = "Error al crear la PagoSocio"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }

        }

        //--------------------------------------------------------------------------------------------------------------

        get("/obtenerPagoSocioesPorCliente/{idCliente}") {

            val apiResponse = ApiResponse<List<PagoSocioDTO>>()
            val idSocio = call.parameters["idSocio"]

            if (idSocio != null) {

                try {

                    val pagoSocios = _repository.obtenerPagoSocioPorSocio(idSocio)
                    val responseDTOs = pagoSocios.map { it.toDto() }

                    apiResponse.success = true
                    apiResponse.message = "PagoSocios obtenidas exitosamente"
                    apiResponse.data = responseDTOs

                    call.respond(HttpStatusCode.OK, apiResponse)

                } catch (e: Exception) {

                    apiResponse.success = false
                    apiResponse.message = "Error al obtener las PagoSocios"
                    apiResponse.errors = listOf(e.message ?: "Error desconocido")

                    call.respond(HttpStatusCode.InternalServerError, apiResponse)
                }

            } else {

                apiResponse.success = false
                apiResponse.message = "ID de Socio no proporcionado"
                apiResponse.errors = listOf("El parámetro 'idSocio' es obligatorio")

                call.respond(HttpStatusCode.BadRequest, apiResponse)
            }
        }

        //--------------------------------------------------------------------------------------------------------------

        get("/obtenerPagoSocioPorId/{idPagoSocio}") {

            val apiResponse = ApiResponse<PagoSocioDTO>()
            val idPagoSocio = call.parameters["idPagoSocio"]

            if (idPagoSocio != null) {

                try {

                    val pagoSocio = _repository.obtenerPagoSocioPorId(idPagoSocio)
                    if (pagoSocio != null) {
                        val responseDTO = pagoSocio.toDto()

                        apiResponse.success = true
                        apiResponse.message = "PagoSocio obtenida exitosamente"
                        apiResponse.data = responseDTO

                        call.respond(HttpStatusCode.OK, apiResponse)

                    } else {

                        apiResponse.success = false
                        apiResponse.message = "PagoSocio no encontrada"
                        apiResponse.errors = listOf("No existe una PagoSocio con el ID proporcionado")

                        call.respond(HttpStatusCode.NotFound, apiResponse)
                    }
                } catch (e: Exception) {

                    apiResponse.success = false
                    apiResponse.message = "Error al obtener la PagoSocio"
                    apiResponse.errors = listOf(e.message ?: "Error desconocido")

                    call.respond(HttpStatusCode.InternalServerError, apiResponse)
                }

            } else {

                apiResponse.success = false
                apiResponse.message = "ID de dirección no proporcionado"
                apiResponse.errors = listOf("El parámetro 'idPagoSocio' es obligatorio")

                call.respond(HttpStatusCode.BadRequest, apiResponse)
            }
        }

        //--------------------------------------------------------------------------------------------------------------

        put("/actualizarPagoSocio") {

            val apiResponse = ApiResponse<Unit>()

            try {

                val pagoSocioDto = call.receive<PagoSocioDTO>()

                val id = pagoSocioDto.idPagoSocio

                if (id != null) {

                    val pagoSocio = pagoSocioDto.toPagoSocio()

                    val pagoSocioEditada = _repository.actualizarPagoSocio(id, pagoSocio)

                    if (pagoSocioEditada) {

                        apiResponse.success = true
                        apiResponse.message = "PagoSocio actualizada"

                        call.respond(HttpStatusCode.OK, apiResponse)

                    } else {

                        apiResponse.success = false
                        apiResponse.message = "PagoSocio no encontrada"
                        apiResponse.errors = listOf("No existe una PagoSocio con el ID proporcionado")

                        call.respond(HttpStatusCode.NotFound, apiResponse)
                    }

                } else {

                    apiResponse.success = false
                    apiResponse.message = "ID de PagoSocio no proporcionado"
                    apiResponse.errors = listOf("No se proporciono ningun id de PagoSocio")

                    call.respond(HttpStatusCode.BadRequest, apiResponse)
                }

            } catch (e: Exception){

                apiResponse.success = false
                apiResponse.message = "Error al actualizar el cliente"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }
        }

        //--------------------------------------------------------------------------------------------------------------

        delete("/eliminarPagoSocio/{id}") {

            val apiResponse = ApiResponse<Unit>()

            val id = call.parameters["id"]

            if (id != null) {

                try {

                    val eliminado = _repository.eliminarPagoSocio(id)

                    if (eliminado) {

                        apiResponse.success = true
                        apiResponse.message = "PagoSocio eliminada"

                        call.respond(HttpStatusCode.OK, apiResponse)

                    } else {

                        apiResponse.success = false
                        apiResponse.message = "PagoSocio no encontrada"
                        apiResponse.errors = listOf("No existe una PagoSocio con el ID proporcionado")

                        call.respond(HttpStatusCode.NotFound, apiResponse)
                    }

                } catch (e: Exception){

                    apiResponse.success = false
                    apiResponse.message = "Error al eliminar la PagoSocio"
                    apiResponse.errors = listOf(e.message ?: "Error desconocido")

                    call.respond(HttpStatusCode.InternalServerError, apiResponse)
                }

            } else {

                apiResponse.success = false
                apiResponse.message = "ID PagoSocio no proporcionado"
                apiResponse.errors = listOf("No se proporciono ningun id de PagoSocio")

                call.respond(HttpStatusCode.BadRequest, apiResponse)
            }
        }

    }
}