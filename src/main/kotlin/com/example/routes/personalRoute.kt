package com.example.routes


import com.example.ApiResponse.ApiResponse
import com.example.DTOs.PersonalDTO
import com.example.Mappers.toDto
import com.example.Mappers.toPersonal
import com.example.repository.interfaces.IPersonalRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route

fun Route.personalRouting(_repository: IPersonalRepository){

    route("/personal") {

        //--------------------------------------------------------------------------------------------------------------

        // Crear un nuevo Personal
        post("/crearPersonal") {

            val apiResponse = ApiResponse< PersonalDTO>()

            try {

                val personalDTO = call.receive<PersonalDTO>()
                val personal = personalDTO.toPersonal()
                val nuevoPersonal = _repository.crearPersonal(personal)
                val responseDto = nuevoPersonal.toDto()

                apiResponse.success = true
                apiResponse.message = "Personal creado exitosamente"
                apiResponse.data = responseDto

                call.respond(HttpStatusCode.Created, apiResponse)

            } catch (e: Exception){

                apiResponse.success = false
                apiResponse.message = "Error al crear Personal"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }

        }

        //--------------------------------------------------------------------------------------------------------------

        // Obtener Personal
        get("/obtenerPersonal") {

            val apiResponse = ApiResponse<List<PersonalDTO>>()

            try {

                val obtenerPersonal = _repository.obtenerPersonal()
                val responseDto = obtenerPersonal.map { it.toDto() }

                apiResponse.success = true
                apiResponse.message = "Personal obtenido exitosamente"
                apiResponse.data = responseDto

                call.respond(HttpStatusCode.OK , apiResponse)

            } catch (e: Exception){

                apiResponse.success = false
                apiResponse.message = "Error al obtener Personal"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }

        }

        //--------------------------------------------------------------------------------------------------------------

        // Obtener un Personal por ID
        get("/obtenerPersonalId/{id}") {

            val apiResponse = ApiResponse<PersonalDTO>()

            try {

                val id = call.parameters["id"]

                if (id != null) {

                    val personal = _repository.obtenerPersonalPorId(id)

                    if (personal != null) {

                        val responseDto = personal.toDto()

                        apiResponse.success = true
                        apiResponse.message = "Personal por id: $id"
                        apiResponse.data = responseDto

                        call.respond(HttpStatusCode.OK ,apiResponse)

                    } else {

                        call.respond(HttpStatusCode.NotFound, "Personal no encontrado")
                    }

                } else {

                    call.respond(HttpStatusCode.BadRequest, "ID del Personal no proporcionado")
                }

            } catch (e: Exception){

                apiResponse.success = false
                apiResponse.message = "Error al obtener Personal"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }

        }

        //--------------------------------------------------------------------------------------------------------------

        // Actualizar un Personal
        put("/actualizarPersonal") {

            val apiResponse = ApiResponse<Unit>()

            try {

                val personalDTO = call.receive<PersonalDTO>()

                val id = personalDTO.idPersonal

                if (id != null) {

                    val personal = personalDTO.toPersonal()

                    val personalEditado = _repository.actualizarPersonal(id, personal)

                    if (personalEditado) {

                        apiResponse.success = true
                        apiResponse.message = "Personal actualizado"

                        call.respond(HttpStatusCode.OK, apiResponse)

                    } else {

                        apiResponse.success = false
                        apiResponse.message = "Personal no encontrado"
                        apiResponse.errors = listOf("No existe un personal con el ID proporcionado")

                        call.respond(HttpStatusCode.NotFound, apiResponse)
                    }

                } else {

                    apiResponse.success = false
                    apiResponse.message = "ID de Personal no proporcionado"
                    apiResponse.errors = listOf("No se proporciono ningun id de Personal")

                    call.respond(HttpStatusCode.BadRequest, apiResponse)
                }

            } catch (e: Exception){

                apiResponse.success = false
                apiResponse.message = "Error al actualizar el Personal"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }
        }

        //--------------------------------------------------------------------------------------------------------------

        // Eliminar un Personal
        delete("/eliminarPersonal/{id}") {

            val apiResponse = ApiResponse<Unit>()

            val id = call.parameters["id"]

            if (id != null) {

                try {

                    val eliminado = _repository.eliminarPersonal(id)

                    if (eliminado) {

                        apiResponse.success = true
                        apiResponse.message = "Personal eliminado"

                        call.respond(HttpStatusCode.OK, apiResponse)

                    } else {

                        apiResponse.success = false
                        apiResponse.message = "Personal no encontrado"
                        apiResponse.errors = listOf("No existe un Personal con el ID proporcionado")

                        call.respond(HttpStatusCode.NotFound, apiResponse)
                    }

                } catch (e: Exception){

                    apiResponse.success = false
                    apiResponse.message = "Error al eliminar el Personal"
                    apiResponse.errors = listOf(e.message ?: "Error desconocido")

                    call.respond(HttpStatusCode.InternalServerError, apiResponse)
                }

            } else {

                apiResponse.success = false
                apiResponse.message = "ID Personal no proporcionado"
                apiResponse.errors = listOf("No se proporciono ningun id de Personal")

                call.respond(HttpStatusCode.BadRequest, apiResponse)
            }
        }

    }




}