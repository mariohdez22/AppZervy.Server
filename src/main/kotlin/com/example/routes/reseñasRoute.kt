package com.example.routes


import com.example.ApiResponse.ApiResponse
import com.example.DTOs.ReseñasDTO
import com.example.Mappers.toDto
import com.example.Mappers.toReseñas
import com.example.repository.interfaces.IReseñasRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route


fun Route.reseñasRouting(_repository: IReseñasRepository){

    route("/reseñas") {

        //--------------------------------------------------------------------------------------------------------------

        // Crear una nueva Reseña
        post("/crearReseña") {

            val apiResponse = ApiResponse< ReseñasDTO>()

            try {

                val reseñasDTO = call.receive<ReseñasDTO>()
                val Reseñas = reseñasDTO.toReseñas()
                val nuevoReseñas = _repository.crearReseñas(Reseñas)
                val responseDto = nuevoReseñas.toDto()

                apiResponse.success = true
                apiResponse.message = "Reseña creada exitosamente"
                apiResponse.data = responseDto

                call.respond(HttpStatusCode.Created, apiResponse)

            } catch (e: Exception){

                apiResponse.success = false
                apiResponse.message = "Error al crear Reseña"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }

        }

        //--------------------------------------------------------------------------------------------------------------


        // Obtener una nueva Reseña por cliente

        get("/obtenerReseñasPorCliente/{idCliente}") {

            val apiResponse = ApiResponse<List<ReseñasDTO>>()
            val idCliente = call.parameters["idCliente"]

            if (idCliente != null) {

                try {

                    val reseñas = _repository.obtenerReseñasPorCliente(idCliente)
                    val responseDTOs = reseñas.map { it.toDto() }

                    apiResponse.success = true
                    apiResponse.message = "Reseñas obtenidas exitosamente"
                    apiResponse.data = responseDTOs

                    call.respond(HttpStatusCode.OK, apiResponse)

                } catch (e: Exception) {

                    apiResponse.success = false
                    apiResponse.message = "Error al obtener las Reseñas"
                    apiResponse.errors = listOf(e.message ?: "Error desconocido")

                    call.respond(HttpStatusCode.InternalServerError, apiResponse)
                }

            } else {

                apiResponse.success = false
                apiResponse.message = "ID de Reseñas no proporcionado"
                apiResponse.errors = listOf("El parámetro 'idReseñas' es obligatorio")

                call.respond(HttpStatusCode.BadRequest, apiResponse)
            }
        }

        //--------------------------------------------------------------------------------------------------------------
        // Obtener una nueva Reseña por Socio

        get("/obtenerReseñasPorSocio/{idSocio}") {

            val apiResponse = ApiResponse<List<ReseñasDTO>>()
            val idSocio = call.parameters["idSocio"]

            if (idSocio != null) {

                try {

                    val reseñas = _repository.obtenerReseñasPorCliente(idSocio)
                    val responseDTOs = reseñas.map { it.toDto() }

                    apiResponse.success = true
                    apiResponse.message = "Reseñas obtenidas exitosamente"
                    apiResponse.data = responseDTOs

                    call.respond(HttpStatusCode.OK, apiResponse)

                } catch (e: Exception) {

                    apiResponse.success = false
                    apiResponse.message = "Error al obtener las Reseñas"
                    apiResponse.errors = listOf(e.message ?: "Error desconocido")

                    call.respond(HttpStatusCode.InternalServerError, apiResponse)
                }

            } else {

                apiResponse.success = false
                apiResponse.message = "ID de Reseñas no proporcionado"
                apiResponse.errors = listOf("El parámetro 'idReseñas' es obligatorio")

                call.respond(HttpStatusCode.BadRequest, apiResponse)
            }
        }

        //--------------------------------------------------------------------------------------------------------------


        // Obtener un Personal por ID
        get("/obtenerReseñasId/{id}") {

            val apiResponse = ApiResponse<ReseñasDTO>()

            try {

                val id = call.parameters["id"]

                if (id != null) {

                    val reseñas = _repository.obtenerReseñasPorId(id)

                    if (reseñas != null) {

                        val responseDto = reseñas.toDto()

                        apiResponse.success = true
                        apiResponse.message = "Reseñas por id: $id"
                        apiResponse.data = responseDto

                        call.respond(HttpStatusCode.OK ,apiResponse)

                    } else {

                        call.respond(HttpStatusCode.NotFound, "Reseña no encontrada")
                    }

                } else {

                    call.respond(HttpStatusCode.BadRequest, "ID de la reseñas no proporcionado")
                }

            } catch (e: Exception){

                apiResponse.success = false
                apiResponse.message = "Error al obtener Reseña"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }

        }

        //--------------------------------------------------------------------------------------------------------------

        // Actualizar una Reseña
        put("/actualizarReseñas") {

            val apiResponse = ApiResponse<Unit>()

            try {

                val reseñasDTO = call.receive<ReseñasDTO>()

                val id = reseñasDTO.idReseñas

                if (id != null) {

                    val reseñas = reseñasDTO.toReseñas()

                    val reseñasEditado = _repository.actualizarReseñas(id, reseñas)

                    if (reseñasEditado) {

                        apiResponse.success = true
                        apiResponse.message = "Reseña actualizada"

                        call.respond(HttpStatusCode.OK, apiResponse)

                    } else {

                        apiResponse.success = false
                        apiResponse.message = "Reseña no encontrada"
                        apiResponse.errors = listOf("No existe una Reseñas con el ID proporcionado")

                        call.respond(HttpStatusCode.NotFound, apiResponse)
                    }

                } else {

                    apiResponse.success = false
                    apiResponse.message = "ID de Reseñas no proporcionado"
                    apiResponse.errors = listOf("No se proporciono ningun id de Reseñas")

                    call.respond(HttpStatusCode.BadRequest, apiResponse)
                }

            } catch (e: Exception){

                apiResponse.success = false
                apiResponse.message = "Error al actualizar la Reseña"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }
        }

        //--------------------------------------------------------------------------------------------------------------

        // Eliminar una Reseña
        delete("/eliminarReseñas/{id}") {

            val apiResponse = ApiResponse<Unit>()

            val id = call.parameters["id"]

            if (id != null) {

                try {

                    val eliminado = _repository.eliminarReseñas(id)

                    if (eliminado) {

                        apiResponse.success = true
                        apiResponse.message = "Reseña eliminada"

                        call.respond(HttpStatusCode.OK, apiResponse)

                    } else {

                        apiResponse.success = false
                        apiResponse.message = "Reseña no encontrada"
                        apiResponse.errors = listOf("No existe una Reseñas con el ID proporcionado")

                        call.respond(HttpStatusCode.NotFound, apiResponse)
                    }

                } catch (e: Exception){

                    apiResponse.success = false
                    apiResponse.message = "Error al eliminar la Reseña"
                    apiResponse.errors = listOf(e.message ?: "Error desconocido")

                    call.respond(HttpStatusCode.InternalServerError, apiResponse)
                }

            } else {

                apiResponse.success = false
                apiResponse.message = "ID Reseñas no proporcionado"
                apiResponse.errors = listOf("No se proporciono ningun id de Reseña")

                call.respond(HttpStatusCode.BadRequest, apiResponse)
            }
        }

    }

}