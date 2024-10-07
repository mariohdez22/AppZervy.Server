package com.example.routes

import com.example.ApiResponse.ApiResponse
import com.example.DTOs.ClienteDTO
import com.example.Mappers.toCliente
import com.example.Mappers.toDto
import com.example.repository.interfaces.IClienteRepostory
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.clienteRouting(_repository: IClienteRepostory) {

    route("/clientes") {

        //--------------------------------------------------------------------------------------------------------------

        // Crear un nuevo cliente
        post("/crearCliente") {

            val apiResponse = ApiResponse<ClienteDTO>()

            try {

                val clienteDto = call.receive<ClienteDTO>()
                val cliente = clienteDto.toCliente()
                val nuevoCliente = _repository.crearCliente(cliente)
                val responseDto = nuevoCliente.toDto()

                apiResponse.success = true
                apiResponse.message = "Cliente creado exitosamente"
                apiResponse.data = responseDto

                call.respond(HttpStatusCode.Created, apiResponse)

            } catch (e: Exception){

                apiResponse.success = false
                apiResponse.message = "Error al crear cliente"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }

        }

        //--------------------------------------------------------------------------------------------------------------

        // Obtener todos los clientes
        get("/obtenerClientes") {

            val apiResponse = ApiResponse<List<ClienteDTO>>()

            try {

                val obtenerClientes = _repository.obtenerClientes()
                val responseDto = obtenerClientes.map { it.toDto() }

                apiResponse.success = true
                apiResponse.message = "Clientes obtenidos exitosamente"
                apiResponse.data = responseDto

                call.respond(HttpStatusCode.OK , apiResponse)

            } catch (e: Exception){

                apiResponse.success = false
                apiResponse.message = "Error al obtener clientes"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }

        }

        //--------------------------------------------------------------------------------------------------------------

        // Obtener un cliente por ID
        get("/obtenerClienteId/{id}") {

            val apiResponse = ApiResponse<ClienteDTO>()

            try {

                val id = call.parameters["id"]

                if (id != null) {

                    val cliente = _repository.obtenerClientePorId(id)

                    if (cliente != null) {

                        val responseDto = cliente.toDto()

                        apiResponse.success = true
                        apiResponse.message = "Cliente por id: $id"
                        apiResponse.data = responseDto

                        call.respond(HttpStatusCode.OK ,apiResponse)

                    } else {

                        call.respond(HttpStatusCode.NotFound, "Cliente no encontrado")
                    }

                } else {

                    call.respond(HttpStatusCode.BadRequest, "ID de cliente no proporcionado")
                }

            } catch (e: Exception){

                apiResponse.success = false
                apiResponse.message = "Error al obtener cliente"
                apiResponse.errors = listOf(e.message ?: "Error desconocido")

                call.respond(HttpStatusCode.InternalServerError, apiResponse)
            }

        }

        //--------------------------------------------------------------------------------------------------------------

        // Actualizar un cliente
        put("/actualizarCliente") {

            val apiResponse = ApiResponse<Unit>()

            try {

                val clienteDto = call.receive<ClienteDTO>()

                val id = clienteDto.idClienteDto

                if (id != null) {

                    val cliente = clienteDto.toCliente()

                    val clienteEditado = _repository.actualizarCliente(id, cliente)

                    if (clienteEditado) {

                        apiResponse.success = true
                        apiResponse.message = "Cliente actualizado"

                        call.respond(HttpStatusCode.OK, apiResponse)

                    } else {

                        apiResponse.success = false
                        apiResponse.message = "Cliente no encontrado"
                        apiResponse.errors = listOf("No existe un cliente con el ID proporcionado")

                        call.respond(HttpStatusCode.NotFound, apiResponse)
                    }

                } else {

                    apiResponse.success = false
                    apiResponse.message = "ID de cliente no proporcionado"
                    apiResponse.errors = listOf("No se proporciono ningun id de cliente")

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

        // Eliminar un cliente
        delete("/eliminarCliente/{id}") {

            val apiResponse = ApiResponse<Unit>()

            val id = call.parameters["id"]

            if (id != null) {

                try {

                    val eliminado = _repository.eliminarCliente(id)

                    if (eliminado) {

                        apiResponse.success = true
                        apiResponse.message = "Cliente eliminado"

                        call.respond(HttpStatusCode.OK, apiResponse)

                    } else {

                        apiResponse.success = false
                        apiResponse.message = "cliente no encontrado"
                        apiResponse.errors = listOf("No existe un cliente con el ID proporcionado")

                        call.respond(HttpStatusCode.NotFound, apiResponse)
                    }

                } catch (e: Exception){

                    apiResponse.success = false
                    apiResponse.message = "Error al eliminar el cliente"
                    apiResponse.errors = listOf(e.message ?: "Error desconocido")

                    call.respond(HttpStatusCode.InternalServerError, apiResponse)
                }

            } else {

                apiResponse.success = false
                apiResponse.message = "ID cliente no proporcionado"
                apiResponse.errors = listOf("No se proporciono ningun id de cliente")

                call.respond(HttpStatusCode.BadRequest, apiResponse)
            }



        }
    }
}

//----------------------------------------------------------------------------------------------------------------------

/*
suspend fun obtenerClientes(call: ApplicationCall) {

    val db = FirestoreClient.getFirestore()
    val clientesRef = db.collection("clientes")
    val snapshot = clientesRef.get().get()
    val clientes = snapshot.documents.map { document ->
        document.toObject(Cliente::class.java)?.copy(idCliente = document.id)
    }.filterNotNull()
    call.respond(clientes)
}

suspend fun obtenerClienteId(call: ApplicationCall) {

    val id = call.parameters["id"]
    if (id != null) {
        val db = FirestoreClient.getFirestore()
        val docRef = db.collection("clientes").document(id)
        val snapshot = docRef.get().get()
        if (snapshot.exists()) {
            val cliente = snapshot.toObject(Cliente::class.java)?.copy(idCliente = snapshot.id)
            call.respond(cliente!!)
        } else {
            call.respond(HttpStatusCode.NotFound, "Cliente no encontrado")
        }
    } else {
        call.respond(HttpStatusCode.BadRequest, "ID de cliente no proporcionado")
    }
}

suspend fun crearCliente(call: ApplicationCall) {

    val cliente = call.receive<Cliente>()
    val db = FirestoreClient.getFirestore()
    val docRef = db.collection("clientes").document()
    val nuevoCliente = cliente.copy(idCliente = docRef.id)
    val result = docRef.set(nuevoCliente).get()
    call.respond(HttpStatusCode.Created, nuevoCliente)
}

suspend fun actualizarCliente(call: ApplicationCall) {

    val id = call.parameters["id"]
    if (id != null) {
        val cliente = call.receive<Cliente>()
        val db = FirestoreClient.getFirestore()
        val docRef = db.collection("clientes").document(id)
        val updatedCliente = cliente.copy(idCliente = id)
        val result = docRef.set(updatedCliente).get()
        call.respond(HttpStatusCode.OK, "Cliente actualizado")
    } else {
        call.respond(HttpStatusCode.BadRequest, "ID de cliente no proporcionado")
    }
}

suspend fun eliminarCliente(call: ApplicationCall) {

    val id = call.parameters["id"]
    if (id != null) {
        val db = FirestoreClient.getFirestore()
        val docRef = db.collection("clientes").document(id)
        val result = docRef.delete().get()
        call.respond(HttpStatusCode.OK, "Cliente eliminado")
    } else {
        call.respond(HttpStatusCode.BadRequest, "ID de cliente no proporcionado")
    }
}
*/


