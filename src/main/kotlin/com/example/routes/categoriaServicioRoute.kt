package com.example.routes

import com.example.ApiResponse.ApiResponse
import com.example.DTOs.CategoriaServicioDTO
import com.example.Mappers.toCategoriaServicio
import com.example.Mappers.toDto
import com.example.repository.interfaces.ICategoriaServicioRepository
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.categoriaServicioRouting(_repository: ICategoriaServicioRepository){

    route("/categoriasServicios"){

        //--------------------------------------------------------------------------------------------------------------

        // Crear un nuevo cliente
        post("/crearCategoriaServicio") {

            val apiResponse = ApiResponse<CategoriaServicioDTO>()

            try {

                val categoriaDto = call.receive<CategoriaServicioDTO>()
                val categoria = categoriaDto.toCategoriaServicio()
                val nuevaCategoria = _repository.crearCategoriaServicio(categoria)
                val responseDto = nuevaCategoria.toDto()

                apiResponse.success = true
                apiResponse.message = "Categoria de servicio creado exitosamente"
                apiResponse.data = responseDto

                call.respond(HttpStatusCode.Created, apiResponse)

            } catch (e: Exception){

                apiResponse.success = false
                apiResponse.message = "Error al crear categoria de servicio"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }

        }

        //--------------------------------------------------------------------------------------------------------------

        // Obtener todos los clientes
        get("/obtenerCategoriaServicio") {

            val apiResponse = ApiResponse<List<CategoriaServicioDTO>>()

            try {

                val obtenerCategoria = _repository.obtenerCategoriaServicio()
                val responseDto = obtenerCategoria.map { it.toDto() }

                apiResponse.success = true
                apiResponse.message = "Categoria de servicio obtenida exitosamente"
                apiResponse.data = responseDto

                call.respond(HttpStatusCode.OK , apiResponse)

            } catch (e: Exception){

                apiResponse.success = false
                apiResponse.message = "Error al obtener categoria de servicio"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }

        }

        //--------------------------------------------------------------------------------------------------------------

        // Obtener un cliente por ID
        get("/obtenerCategoriaServicioId/{id}") {

            val apiResponse = ApiResponse<CategoriaServicioDTO>()

            try {

                val id = call.parameters["id"]

                if (id != null) {

                    val categoria = _repository.obtenerCategoriaServicioPorId(id)

                    if (categoria != null) {

                        val responseDto = categoria.toDto()

                        apiResponse.success = true
                        apiResponse.message = "Categoria servicio por id: $id"
                        apiResponse.data = responseDto

                        call.respond(HttpStatusCode.OK ,apiResponse)

                    } else {

                        call.respond(HttpStatusCode.NotFound, "Categoria no encontrado")
                    }

                } else {

                    call.respond(HttpStatusCode.BadRequest, "ID de categoria no proporcionado")
                }

            } catch (e: Exception){

                apiResponse.success = false
                apiResponse.message = "Error al obtener categoria de servicio"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }

        }

        //--------------------------------------------------------------------------------------------------------------

        // Actualizar un cliente
        put("/actualizarCategoriaServicio") {

            val apiResponse = ApiResponse<Unit>()

            try {

                val categoriaDto = call.receive<CategoriaServicioDTO>()

                val id = categoriaDto.idCategoriaServicio

                if (id != null) {

                    val categoria = categoriaDto.toCategoriaServicio()

                    val categoriaEditado = _repository.actualizarCategoriaServicio(id, categoria)

                    if (categoriaEditado) {

                        apiResponse.success = true
                        apiResponse.message = "Categoria actualizado"

                        call.respond(HttpStatusCode.OK, apiResponse)

                    } else {

                        apiResponse.success = false
                        apiResponse.message = "Categoria no encontrado"
                        apiResponse.errors = listOf("No existe una categoria con el ID proporcionado")

                        call.respond(HttpStatusCode.NotFound, apiResponse)
                    }

                } else {

                    apiResponse.success = false
                    apiResponse.message = "ID de categoria no proporcionado"
                    apiResponse.errors = listOf("No se proporciono ningun id de categoria")

                    call.respond(HttpStatusCode.BadRequest, apiResponse)
                }

            } catch (e: Exception){

                apiResponse.success = false
                apiResponse.message = "Error al actualizar la categoria"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }
        }

        //--------------------------------------------------------------------------------------------------------------

        // Eliminar un cliente
        delete("/eliminarCategoriaServicio/{id}") {

            val apiResponse = ApiResponse<Unit>()

            val id = call.parameters["id"]

            if (id != null) {

                try {

                    val eliminado = _repository.eliminarCategoriaServicio(id)

                    if (eliminado) {

                        apiResponse.success = true
                        apiResponse.message = "Categoria eliminado"

                        call.respond(HttpStatusCode.OK, apiResponse)

                    } else {

                        apiResponse.success = false
                        apiResponse.message = "Categoria no encontrado"
                        apiResponse.errors = listOf("No existe una categoria con el ID proporcionado")

                        call.respond(HttpStatusCode.NotFound, apiResponse)
                    }

                } catch (e: Exception){

                    apiResponse.success = false
                    apiResponse.message = "Error al eliminar la categoria"
                    apiResponse.errors = listOf(e.message ?: "Error desconocido")

                    call.respond(HttpStatusCode.InternalServerError, apiResponse)
                }

            } else {

                apiResponse.success = false
                apiResponse.message = "ID categoria no proporcionada"
                apiResponse.errors = listOf("No se proporciono ningun id de categoria")

                call.respond(HttpStatusCode.BadRequest, apiResponse)
            }
        }

    }
}