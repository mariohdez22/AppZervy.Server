package com.example.routes

import com.example.ApiResponse.ApiResponse
import com.example.DTOs.FotoSolicitudDTO
import com.example.Mappers.toDto
import com.example.Mappers.toFotoSolicitud
import com.example.repository.interfaces.IFotoSolicitudRepository
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.fotoSolicitudRouting(_repository : IFotoSolicitudRepository) {

    route("/fotoSolicitud") {

        //--------------------------------------------------------------------------------------------------------------

        post("/crearFotoSolicitud") {

            val apiResponse = ApiResponse<FotoSolicitudDTO>()

            try {

                val fotoSolicitudDTO = call.receive<FotoSolicitudDTO>()
                val fotoSolicitud = fotoSolicitudDTO.toFotoSolicitud()
                val nuevaFotoSolicitud = _repository.crearFotoSolicitud(fotoSolicitud)
                val responseDTO = nuevaFotoSolicitud.toDto()

                apiResponse.success = true
                apiResponse.message = "Foto Solicitud creada exitosamente"
                apiResponse.data = responseDTO

                call.respond(HttpStatusCode.Created, apiResponse)

            } catch (e: Exception) {

                apiResponse.success = false
                apiResponse.message = "Error al crear la Foto Solicitud"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }

        }

        //--------------------------------------------------------------------------------------------------------------

        get("/obtenerFotoSolicitudesPorSolicitud/{idSolicitud}") {

            val apiResponse = ApiResponse<List<FotoSolicitudDTO>>()
            val idSolicitud = call.parameters["idSolicitud"]

            if (idSolicitud != null) {

                try {

                    val fotoSolicitudes = _repository.obtenerFotoSolicitudesPorSolicitud(idSolicitud)
                    val responseDTOs = fotoSolicitudes.map { it.toDto() }

                    apiResponse.success = true
                    apiResponse.message = "Fotos Solicitudes obtenidas exitosamente"
                    apiResponse.data = responseDTOs

                    call.respond(HttpStatusCode.OK, apiResponse)

                } catch (e: Exception) {

                    apiResponse.success = false
                    apiResponse.message = "Error al obtener las Fotos Solicitudes"
                    apiResponse.errors = listOf(e.message ?: "Error desconocido")

                    call.respond(HttpStatusCode.InternalServerError, apiResponse)
                }

            } else {

                apiResponse.success = false
                apiResponse.message = "ID de solicitud no proporcionado"
                apiResponse.errors = listOf("El parámetro 'idSolicitud' es obligatorio")

                call.respond(HttpStatusCode.BadRequest, apiResponse)
            }
        }

        //--------------------------------------------------------------------------------------------------------------

        get("/obtenerFotoSolicitudPorId/{idFotoSolicitud}") {

            val apiResponse = ApiResponse<FotoSolicitudDTO>()
            val idFotoSolicitud = call.parameters["idFotoSolicitud"]

            if (idFotoSolicitud != null) {

                try {

                    val fotoSolicitud = _repository.obtenerFotoSolicitudPorId(idFotoSolicitud)
                    if (fotoSolicitud != null) {
                        val responseDTO = fotoSolicitud.toDto()

                        apiResponse.success = true
                        apiResponse.message = "Foto Solicitud obtenida exitosamente"
                        apiResponse.data = responseDTO

                        call.respond(HttpStatusCode.OK, apiResponse)

                    } else {

                        apiResponse.success = false
                        apiResponse.message = "Foto Solicitud no encontrada"
                        apiResponse.errors = listOf("No existe una foto con el ID proporcionado")

                        call.respond(HttpStatusCode.NotFound, apiResponse)
                    }
                } catch (e: Exception) {

                    apiResponse.success = false
                    apiResponse.message = "Error al obtener la foto Solicitud"
                    apiResponse.errors = listOf(e.message ?: "Error desconocido")

                    call.respond(HttpStatusCode.InternalServerError, apiResponse)
                }

            } else {

                apiResponse.success = false
                apiResponse.message = "ID de foto solicitud no proporcionado"
                apiResponse.errors = listOf("El parámetro 'idFotoSolicitud' es obligatorio")

                call.respond(HttpStatusCode.BadRequest, apiResponse)
            }
        }

        //--------------------------------------------------------------------------------------------------------------

        put("/actualizarFotoSolicitud") {

            val apiResponse = ApiResponse<Unit>()

            try {

                val fotoSolicitudDto = call.receive<FotoSolicitudDTO>()

                val id = fotoSolicitudDto.idFotoSolicitud

                if (id != null) {

                    val fotoSolicitud = fotoSolicitudDto.toFotoSolicitud()

                    val fotoSolicitudEditada = _repository.actualizarFotoSolicitud(id, fotoSolicitud)

                    if (fotoSolicitudEditada) {

                        apiResponse.success = true
                        apiResponse.message = "FotoSolicitud actualizada"

                        call.respond(HttpStatusCode.OK, apiResponse)

                    } else {

                        apiResponse.success = false
                        apiResponse.message = "FotoSolicitud no encontrada"
                        apiResponse.errors = listOf("No existe una Foto Solicitud con el ID proporcionado")

                        call.respond(HttpStatusCode.NotFound, apiResponse)
                    }

                } else {

                    apiResponse.success = false
                    apiResponse.message = "ID de Foto Solicitud no proporcionado"
                    apiResponse.errors = listOf("No se proporciono ningun id de FotoSolicitud")

                    call.respond(HttpStatusCode.BadRequest, apiResponse)
                }

            } catch (e: Exception){

                apiResponse.success = false
                apiResponse.message = "Error al actualizar la foto solicitud"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }
        }

        //--------------------------------------------------------------------------------------------------------------

        delete("/eliminarFotoSolicitud/{id}") {

            val apiResponse = ApiResponse<Unit>()

            val id = call.parameters["id"]

            if (id != null) {

                try {

                    val eliminado = _repository.eliminarFotoSolicitud(id)

                    if (eliminado) {

                        apiResponse.success = true
                        apiResponse.message = "FotoSolicitud eliminada"

                        call.respond(HttpStatusCode.OK, apiResponse)

                    } else {

                        apiResponse.success = false
                        apiResponse.message = "FotoSolicitud no encontrada"
                        apiResponse.errors = listOf("No existe una FotoSolicitud con el ID proporcionado")

                        call.respond(HttpStatusCode.NotFound, apiResponse)
                    }

                } catch (e: Exception){

                    apiResponse.success = false
                    apiResponse.message = "Error al eliminar la FotoSolicitud"
                    apiResponse.errors = listOf(e.message ?: "Error desconocido")

                    call.respond(HttpStatusCode.InternalServerError, apiResponse)
                }

            } else {

                apiResponse.success = false
                apiResponse.message = "ID FotoSolicitud no proporcionado"
                apiResponse.errors = listOf("No se proporciono ningun id de FotoSolicitud")

                call.respond(HttpStatusCode.BadRequest, apiResponse)
            }
        }

    }

}