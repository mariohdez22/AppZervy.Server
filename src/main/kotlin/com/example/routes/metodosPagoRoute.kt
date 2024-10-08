package com.example.routes

import com.example.ApiResponse.ApiResponse
import com.example.DTOs.MetodosPagoDTO
import com.example.Mappers.toDto
import com.example.Mappers.toMetodosPago
import com.example.repository.interfaces.IMetodosPagoRepository
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.metodosPagoRouting(_repository: IMetodosPagoRepository) {

    route("/metodosPago"){

        //--------------------------------------------------------------------------------------------------------------

        post("/crearMetodoPago") {

            val apiResponse = ApiResponse<MetodosPagoDTO>()

            try {

                val metodoPagoDTO = call.receive<MetodosPagoDTO>()
                val metodoPago = metodoPagoDTO.toMetodosPago()
                val nuevoMetodo = _repository.crearMetodoPago(metodoPago)
                val responseDTO = nuevoMetodo.toDto()

                apiResponse.success = true
                apiResponse.message = "Metodo de pago creado exitosamente"
                apiResponse.data = responseDTO

                call.respond(HttpStatusCode.Created, apiResponse)

            } catch (e: Exception) {

                apiResponse.success = false
                apiResponse.message = "Error al crear el metodo de pago"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }

        }

        //--------------------------------------------------------------------------------------------------------------

        get("/obtenerMetodoPagoPorCliente/{idCliente}") {

            val apiResponse = ApiResponse<List<MetodosPagoDTO>>()
            val idCliente = call.parameters["idCliente"]

            if (idCliente != null) {

                try {

                    val metodosPago = _repository.obtenerMetodoPagoPorCliente(idCliente)
                    val responseDTOs = metodosPago.map { it.toDto() }

                    apiResponse.success = true
                    apiResponse.message = "Metodos de pago obtenidos exitosamente"
                    apiResponse.data = responseDTOs

                    call.respond(HttpStatusCode.OK, apiResponse)

                } catch (e: Exception) {

                    apiResponse.success = false
                    apiResponse.message = "Error al obtener los metodos de pago"
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

        get("/obtenerMetodoPagoPorId/{idMetodoPago}") {

            val apiResponse = ApiResponse<MetodosPagoDTO>()
            val idMetodoPago = call.parameters["idMetodoPago"]

            if (idMetodoPago != null) {

                try {

                    val metodoPago = _repository.obtenerMetodoPagoPorId(idMetodoPago)
                    if (metodoPago != null) {
                        val responseDTO = metodoPago.toDto()

                        apiResponse.success = true
                        apiResponse.message = "Metodo de pago obtenido exitosamente"
                        apiResponse.data = responseDTO

                        call.respond(HttpStatusCode.OK, apiResponse)

                    } else {

                        apiResponse.success = false
                        apiResponse.message = "Metodo de pago no encontrado"
                        apiResponse.errors = listOf("No existe un metodo de pago con el ID proporcionado")

                        call.respond(HttpStatusCode.NotFound, apiResponse)
                    }
                } catch (e: Exception) {

                    apiResponse.success = false
                    apiResponse.message = "Error al obtener el metodo de pago"
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

        put("/actualizarMetodoPago") {

            val apiResponse = ApiResponse<Unit>()

            try {

                val metodoPagoDto = call.receive<MetodosPagoDTO>()

                val id = metodoPagoDto.idMetodoPago

                if (id != null) {

                    val metodoPago = metodoPagoDto.toMetodosPago()

                    val metodoEditado = _repository.actualizarMetodosPago(id, metodoPago)

                    if (metodoEditado) {

                        apiResponse.success = true
                        apiResponse.message = "Metodo de pago actualizado"

                        call.respond(HttpStatusCode.OK, apiResponse)

                    } else {

                        apiResponse.success = false
                        apiResponse.message = "Metodo de pago no encontrado"
                        apiResponse.errors = listOf("No existe un metodo de pago con el ID proporcionado")

                        call.respond(HttpStatusCode.NotFound, apiResponse)
                    }

                } else {

                    apiResponse.success = false
                    apiResponse.message = "ID de metodo de pago no proporcionado"
                    apiResponse.errors = listOf("No se proporciono ningun id de metodo de pago")

                    call.respond(HttpStatusCode.BadRequest, apiResponse)
                }

            } catch (e: Exception){

                apiResponse.success = false
                apiResponse.message = "Error al actualizar el metodo de pago"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }
        }

        //--------------------------------------------------------------------------------------------------------------

        delete("/eliminarMetodoPago/{id}") {

            val apiResponse = ApiResponse<Unit>()

            val id = call.parameters["id"]

            if (id != null) {

                try {

                    val eliminado = _repository.eliminarMetodoPago(id)

                    if (eliminado) {

                        apiResponse.success = true
                        apiResponse.message = "Metodo de pago eliminado"

                        call.respond(HttpStatusCode.OK, apiResponse)

                    } else {

                        apiResponse.success = false
                        apiResponse.message = "Metodo de pago no encontrado"
                        apiResponse.errors = listOf("No existe un metodo de pago con el ID proporcionado")

                        call.respond(HttpStatusCode.NotFound, apiResponse)
                    }

                } catch (e: Exception){

                    apiResponse.success = false
                    apiResponse.message = "Error al eliminar el metodo de pago"
                    apiResponse.errors = listOf(e.message ?: "Error desconocido")

                    call.respond(HttpStatusCode.InternalServerError, apiResponse)
                }

            } else {

                apiResponse.success = false
                apiResponse.message = "ID metodo de pago no proporcionado"
                apiResponse.errors = listOf("No se proporciono ningun id de metodo de pago")

                call.respond(HttpStatusCode.BadRequest, apiResponse)
            }
        }

    }
}