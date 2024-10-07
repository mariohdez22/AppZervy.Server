package com.example.routes

import com.example.ApiResponse.ApiResponse
import com.example.DTOs.ClienteDTO
import com.example.DTOs.DireccionDTO
import com.example.Mappers.toCliente
import com.example.Mappers.toDireccion
import com.example.Mappers.toDto
import com.example.repository.interfaces.IDireccionRepository
import com.google.api.gax.retrying.DirectRetryingExecutor
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.direccionRouting(_repository: IDireccionRepository){

    route("/direccion"){

        //--------------------------------------------------------------------------------------------------------------

        post("/crearDireccion") {

            val apiResponse = ApiResponse<DireccionDTO>()

            try {

                val direccionDTO = call.receive<DireccionDTO>()
                val direccion = direccionDTO.toDireccion()
                val nuevaDireccion = _repository.crearDireccion(direccion)
                val responseDTO = nuevaDireccion.toDto()

                apiResponse.success = true
                apiResponse.message = "Dirección creada exitosamente"
                apiResponse.data = responseDTO

                call.respond(HttpStatusCode.Created, apiResponse)

            } catch (e: Exception) {

                apiResponse.success = false
                apiResponse.message = "Error al crear la dirección"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }

        }

        //--------------------------------------------------------------------------------------------------------------

        get("/obtenerDireccionesPorCliente/{idCliente}") {

            val apiResponse = ApiResponse<List<DireccionDTO>>()
            val idCliente = call.parameters["idCliente"]

            if (idCliente != null) {

                try {

                    val direcciones = _repository.obtenerDireccionesPorCliente(idCliente)
                    val responseDTOs = direcciones.map { it.toDto() }

                    apiResponse.success = true
                    apiResponse.message = "Direcciones obtenidas exitosamente"
                    apiResponse.data = responseDTOs

                    call.respond(HttpStatusCode.OK, apiResponse)

                } catch (e: Exception) {

                    apiResponse.success = false
                    apiResponse.message = "Error al obtener las direcciones"
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

        get("/obtenerDireccionPorId/{idDireccion}") {

            val apiResponse = ApiResponse<DireccionDTO>()
            val idDireccion = call.parameters["idDireccion"]

            if (idDireccion != null) {

                try {

                    val direccion = _repository.obtenerDireccionPorId(idDireccion)
                    if (direccion != null) {
                        val responseDTO = direccion.toDto()

                        apiResponse.success = true
                        apiResponse.message = "Dirección obtenida exitosamente"
                        apiResponse.data = responseDTO

                        call.respond(HttpStatusCode.OK, apiResponse)

                    } else {

                        apiResponse.success = false
                        apiResponse.message = "Dirección no encontrada"
                        apiResponse.errors = listOf("No existe una dirección con el ID proporcionado")

                        call.respond(HttpStatusCode.NotFound, apiResponse)
                    }
                } catch (e: Exception) {

                    apiResponse.success = false
                    apiResponse.message = "Error al obtener la dirección"
                    apiResponse.errors = listOf(e.message ?: "Error desconocido")

                    call.respond(HttpStatusCode.InternalServerError, apiResponse)
                }

            } else {

                apiResponse.success = false
                apiResponse.message = "ID de dirección no proporcionado"
                apiResponse.errors = listOf("El parámetro 'idDireccion' es obligatorio")

                call.respond(HttpStatusCode.BadRequest, apiResponse)
            }
        }

        //--------------------------------------------------------------------------------------------------------------

        put("/actualizarDireccion") {

            val apiResponse = ApiResponse<Unit>()

            try {

                val direccionDto = call.receive<DireccionDTO>()

                val id = direccionDto.idDireccion

                if (id != null) {

                    val direccion = direccionDto.toDireccion()

                    val direccionEditada = _repository.actualizarDireccion(id, direccion)

                    if (direccionEditada) {

                        apiResponse.success = true
                        apiResponse.message = "Direccion actualizada"

                        call.respond(HttpStatusCode.OK, apiResponse)

                    } else {

                        apiResponse.success = false
                        apiResponse.message = "Direccion no encontrada"
                        apiResponse.errors = listOf("No existe una direccion con el ID proporcionado")

                        call.respond(HttpStatusCode.NotFound, apiResponse)
                    }

                } else {

                    apiResponse.success = false
                    apiResponse.message = "ID de direccion no proporcionado"
                    apiResponse.errors = listOf("No se proporciono ningun id de direccion")

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

        delete("/eliminarDireccion/{id}") {

            val apiResponse = ApiResponse<Unit>()

            val id = call.parameters["id"]

            if (id != null) {

                try {

                    val eliminado = _repository.eliminarDireccion(id)

                    if (eliminado) {

                        apiResponse.success = true
                        apiResponse.message = "Direccion eliminada"

                        call.respond(HttpStatusCode.OK, apiResponse)

                    } else {

                        apiResponse.success = false
                        apiResponse.message = "Direccion no encontrada"
                        apiResponse.errors = listOf("No existe un cliente con el ID proporcionado")

                        call.respond(HttpStatusCode.NotFound, apiResponse)
                    }

                } catch (e: Exception){

                    apiResponse.success = false
                    apiResponse.message = "Error al eliminar la direccion"
                    apiResponse.errors = listOf(e.message ?: "Error desconocido")

                    call.respond(HttpStatusCode.InternalServerError, apiResponse)
                }

            } else {

                apiResponse.success = false
                apiResponse.message = "ID direccion no proporcionado"
                apiResponse.errors = listOf("No se proporciono ningun id de direccion")

                call.respond(HttpStatusCode.BadRequest, apiResponse)
            }
        }

    }
}