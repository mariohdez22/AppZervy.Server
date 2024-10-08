package com.example.routes

import com.example.ApiResponse.ApiResponse
import com.example.DTOs.PropuestaServicioDTO
import com.example.Mappers.toPropuestaServicio
import com.example.Mappers.toPropuestaServicioDTO
import com.example.repository.interfaces.IPropuestaServicioRepository
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.PropuestaServicio(
    _repo: IPropuestaServicioRepository
){
    route("/propuestas"){

        post(
            "/crearPropuesta"
        ) {

            val apiResponse = ApiResponse<PropuestaServicioDTO>()

            try{

                val propuestaDTO = call.receive<PropuestaServicioDTO>()
                val propuesta = propuestaDTO.toPropuestaServicio()
                val nuevaPropuesta = _repo.crearPropuestaServicio(propuesta)
                val responseDTO = nuevaPropuesta.toPropuestaServicioDTO()

                apiResponse.success = true
                apiResponse.message = "Propuesta servicio creada exitosamente"
                apiResponse.data = responseDTO

                call.respond(HttpStatusCode.Created, apiResponse)

            }catch(e: Exception){

                apiResponse.success = false
                apiResponse.message = "Error al crear propuesta"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }

        }

        get(
            "/obtenerPropuestas"
        ) {

            val apiResponse = ApiResponse<List<PropuestaServicioDTO>>()

            try{

                val propuestas = _repo.obtenerPropuestaServicio()
                val responseDTO = propuestas.map { it.toPropuestaServicioDTO() }

                apiResponse.success = true
                apiResponse.message = "Propuestas obtenidas exitosamente"
                apiResponse.data = responseDTO

                call.respond(HttpStatusCode.OK, apiResponse)

            }catch(e: Exception){

                apiResponse.success = false
                apiResponse.message = "Error al obtener propuestas"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }

        }

        get(
            "/obtenerPropuestaId/{id}"
        ) {
            val apiResponse = ApiResponse<PropuestaServicioDTO>()

            try{

                val id = call.parameters["id"]

                if(id != null){
                    val propuesta = _repo.obtenerPropuestaServicioPorId(id)

                    if(propuesta != null){
                        val responseDTO = propuesta.toPropuestaServicioDTO()

                        apiResponse.success = true
                        apiResponse.message = "Propuesta por id: $id"
                        apiResponse.data = responseDTO

                        call.respond(HttpStatusCode.OK, apiResponse)
                    }else{
                        call.respond(
                            HttpStatusCode.NotFound,
                            "Propuesta servicio no encontrada"
                        )
                    }
                }else{
                    call.respond(
                        HttpStatusCode.BadRequest,
                        "ID de la propuesta no proporcionado"
                    )
                }

            }catch(e: Exception){

                apiResponse.success = false
                apiResponse.message = "Error al crear propuesta"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }
        }

        put(
            "/actualizarPropuesta"
        ) {
            val apiResponse = ApiResponse<Unit>()

            try{

                val propuestaDTO = call.receive<PropuestaServicioDTO>()
                val id = propuestaDTO.idPropuestaDTO

                if(id != null){
                    val propuesta = propuestaDTO.toPropuestaServicio()
                    val propuestaEditado = _repo.actualizarPropuestaServicio(id, propuesta)

                    if(propuestaEditado){
                        apiResponse.success = true
                        apiResponse.message = "Propuesta actualizada"

                        call.respond(HttpStatusCode.OK, apiResponse)
                    }else{
                        apiResponse.success = false
                        apiResponse.message = "Error al actualizar propuesta"
                        apiResponse.errors =
                            listOf("No existe una propuesta con el ID proporcionado")

                        call.respond(HttpStatusCode.NotFound, apiResponse)
                    }
                }

            }catch(e: Exception){
                apiResponse.success = false
                apiResponse.message = "Error al actualizar propuesta"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }
        }

        delete(
            "/eliminarPropuestas/{id}"
        ){
            val apiResponse = ApiResponse<Unit>()
            val id = call.parameters["id"]

            if(id != null){

                try{

                    val eliminado = _repo.eliminarPropuestaServicio(id)

                    if(eliminado){
                        apiResponse.success = true
                        apiResponse.message = "Propuesta eliminada"

                        call.respond(HttpStatusCode.OK, apiResponse)
                    }else{
                        apiResponse.success = false
                        apiResponse.message = "Error al eliminar propuesta"
                        apiResponse.errors =
                            listOf("No existe una propuesta con el ID proporcionado")

                        call.respond(HttpStatusCode.NotFound, apiResponse)
                    }

                }catch(e: Exception){
                    apiResponse.success = false
                    apiResponse.message = "Error al eliminar propuesta"
                    apiResponse.errors = listOf(e.message ?: "Error desconocido")

                    call.respond(HttpStatusCode.InternalServerError, apiResponse)
                }

            }else{
                apiResponse.success = false
                apiResponse.message = "ID propuesta no proporcionado"
                apiResponse.errors = listOf("No se proporciono ningun id de la propuesta")

                call.respond(HttpStatusCode.BadRequest, apiResponse)
            }
        }

    }
}