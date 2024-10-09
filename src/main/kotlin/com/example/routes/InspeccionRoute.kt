package com.example.routes

import com.example.ApiResponse.ApiResponse
import com.example.DTOs.DireccionDTO
import com.example.DTOs.InspeccionDTO
import com.example.Mappers.toDireccion
import com.example.Mappers.toDto
import com.example.Mappers.toInspeccion
import com.example.Mappers.toInspeccionDTO
import com.example.repository.interfaces.IInspeccionRepository
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.awt.event.InputEvent

fun Route.InspeccionRouting(
    _repo: IInspeccionRepository
) {

    route("/inspeccion") {

        post("/crearInspeccion") {

            val apiResponse = ApiResponse<InspeccionDTO>()

            try {

                val inspeccionDTO = call.receive<InspeccionDTO>()
                val inspeccion = inspeccionDTO.toInspeccion()
                val nuevaInspeccion = _repo.crearInspeccion(inspeccion)
                val responseDTO = nuevaInspeccion.toInspeccionDTO()

                apiResponse.success = true
                apiResponse.message = "Inspeccion creada exitosamente"
                apiResponse.data = responseDTO

                call.respond(HttpStatusCode.Created, apiResponse)

            } catch (e: Exception) {

                apiResponse.success = false
                apiResponse.message = "Error al crear la inspeccion"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }

        }

        get("/obtenerInspeccionPorPropuesta/{idPropuesta}") {

            val apiResponse = ApiResponse<List<InspeccionDTO>>()
            val idPropuesta = call.parameters["idPropuesta"]

            if (idPropuesta != null) {

                try {

                    val inspecciones = _repo.obtenerInspeccionPorPropuesta(idPropuesta)
                    val responseDTOs = inspecciones.map { it.toInspeccionDTO() }

                    apiResponse.success = true
                    apiResponse.message = "Inspeccion obtenidas exitosamente"
                    apiResponse.data = responseDTOs

                    call.respond(HttpStatusCode.OK, apiResponse)

                } catch (e: Exception) {

                    apiResponse.success = false
                    apiResponse.message = "Error al obtener las inspecciones"
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


        get("/obtenerInspeccionPorId/{codInspeccion}") {

            val apiResponse = ApiResponse<InspeccionDTO>()
            val codInspeccion = call.parameters["codInspeccion"]

            if (codInspeccion != null) {

                try {

                    val inspeccion = _repo.obtenerInspeccionPorId(codInspeccion)
                    if (inspeccion != null) {
                        val responseDTO = inspeccion.toInspeccionDTO()

                        apiResponse.success = true
                        apiResponse.message = "Inspeccion obtenida exitosamente"
                        apiResponse.data = responseDTO

                        call.respond(HttpStatusCode.OK, apiResponse)

                    } else {

                        apiResponse.success = false
                        apiResponse.message = "Inspeccion no encontrada"
                        apiResponse.errors =
                            listOf("No existe una inspeccion con el codigo proporcionado")

                        call.respond(HttpStatusCode.NotFound, apiResponse)
                    }
                } catch (e: Exception) {

                    apiResponse.success = false
                    apiResponse.message = "Error al obtener la inspeccion"
                    apiResponse.errors = listOf(e.message ?: "Error desconocido")

                    call.respond(HttpStatusCode.InternalServerError, apiResponse)
                }

            } else {

                apiResponse.success = false
                apiResponse.message = "Codigo de inspeccion no proporcionado"
                apiResponse.errors = listOf("El parámetro 'codInspeccion' es obligatorio")

                call.respond(HttpStatusCode.BadRequest, apiResponse)
            }
        }


    }

    put("/actualizarInspeccion") {

        val apiResponse = ApiResponse<Unit>()

        try {
            val inspeccionDTO = call.receive<InspeccionDTO>()
            val id = inspeccionDTO.codInspeccionDTO

            if (id != null) {
                val inspeccion = inspeccionDTO.toInspeccion()
                val inspeccionEditada = _repo.actualizarInspeccion(id, inspeccion)

                if (inspeccionEditada) {

                    apiResponse.success = true
                    apiResponse.message = "Inspeccion actualizada"

                    call.respond(HttpStatusCode.OK, apiResponse)

                } else {

                    apiResponse.success = false
                    apiResponse.message = "Inspeccion no encontrada"
                    apiResponse.errors =
                        listOf("No existe una inspeccion con el codigo proporcionado")

                    call.respond(HttpStatusCode.NotFound, apiResponse)
                }

            } else {

                apiResponse.success = false
                apiResponse.message = "Codigo de direccion no proporcionado"
                apiResponse.errors = listOf("No se proporciono ningun codigo de inspeccion")

                call.respond(HttpStatusCode.BadRequest, apiResponse)
            }

        } catch (e: Exception){

            apiResponse.success = false
            apiResponse.message = "Error al actualizar la inspeccion"
            apiResponse.errors = listOf(e.message ?: "Error desconocido")

            call.respond(HttpStatusCode.InternalServerError, apiResponse)
        }
    }

    delete("/eliminarInspeccion/{codInspeccion}") {

        val apiResponse = ApiResponse<Unit>()

        val id = call.parameters["codInspeccion"]

        if (id != null) {

            try {

                val eliminado = _repo.eliminarInspeccion(id)

                if (eliminado) {

                    apiResponse.success = true
                    apiResponse.message = "Inspeccion eliminada"

                    call.respond(HttpStatusCode.OK, apiResponse)

                } else {

                    apiResponse.success = false
                    apiResponse.message = "Inspeccion no encontrada"
                    apiResponse.errors =
                        listOf("No existe una inspeccion con el codigo proporcionado")

                    call.respond(HttpStatusCode.NotFound, apiResponse)
                }

            } catch (e: Exception){

                apiResponse.success = false
                apiResponse.message = "Error al eliminar la inspeccion"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }

        } else {

            apiResponse.success = false
            apiResponse.message = "Codigo de inspeccion no proporcionado"
            apiResponse.errors = listOf("No se proporciono ningun codigo de inspeccion")

            call.respond(HttpStatusCode.BadRequest, apiResponse)
        }
    }

}