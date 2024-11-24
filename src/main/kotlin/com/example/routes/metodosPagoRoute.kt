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

        //Endpoint para crear metodo de pago
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

        //Endpoint para obtener metodo por cliente
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

        //Endpoint para obtener metodo por id
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

        //Endpoint para actualizar metodo de pago
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

        //Endpoint para eliminar metodo de pago
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


        //Endpoint para actualizar metodo de pago asociado a cliente y socio
        put("/actualizarMetodoPagoSocio") {

            val apiResponse = ApiResponse<MetodosPagoDTO>()

            try {
                val metodoPagoDto = call.receive<MetodosPagoDTO>()

                // Validar IDs necesarios
                val idMetodoPago = metodoPagoDto.idMetodoPago
                val idSocio = metodoPagoDto.idSocio
                val idCliente = metodoPagoDto.idCliente

                if (idMetodoPago == null || idSocio == null || idCliente == null) {
                    apiResponse.success = false
                    apiResponse.message = "IDs faltantes"
                    apiResponse.errors = listOf(
                        "ID de método de pago, cliente y socio son obligatorios"
                    )
                    call.respond(HttpStatusCode.BadRequest, apiResponse)
                    return@put
                }

                // Convertir DTO a modelo
                val metodoPago = metodoPagoDto.toMetodosPago()

                // Actualizar el método de pago existente
                val metodoActualizadoCliente = _repository.actualizarMetodosPago(idMetodoPago, metodoPago)

                if (!metodoActualizadoCliente) {
                    apiResponse.success = false
                    apiResponse.message = "No se pudo actualizar el método de pago para el cliente"
                    apiResponse.errors = listOf("El método de pago con ID $idMetodoPago no existe")
                    call.respond(HttpStatusCode.NotFound, apiResponse)
                    return@put
                }

                // Asociar el método de pago al socio
                val metodoActualizadoSocio = _repository.agregarMetodoPagoPorSocio(idSocio, metodoPago)

                val responseDTO = metodoActualizadoSocio.toDto()

                apiResponse.success = true
                apiResponse.message = "Método de pago actualizado exitosamente para cliente y socio"
                apiResponse.data = responseDTO

                call.respond(HttpStatusCode.OK, apiResponse)

            } catch (e: Exception) {

                apiResponse.success = false
                apiResponse.message = "Error al actualizar el método de pago para cliente y socio"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")
                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }
        }

    }
}