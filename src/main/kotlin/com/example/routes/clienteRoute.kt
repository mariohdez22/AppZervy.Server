package com.example.routes

import com.example.ApiResponse.ApiResponse
import com.example.DTOs.ClienteDTO
import com.example.Mappers.toCliente
import com.example.Mappers.toDto
import com.example.repository.interfaces.IClienteRepostory
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.example.plugins.FirebasePrincipal // Asegúrate de importar FirebasePrincipal

fun Route.clienteRouting(_repository: IClienteRepostory) {

    authenticate("firebase-auth") { // Asegúrate de que las rutas están dentro del bloque authenticate
        route("/clientes") {

            //--------------------------------------------------------------------------------------------------------------

            // Crear un nuevo cliente
            post("/crearCliente") {
                val apiResponse = ApiResponse<ClienteDTO>()

                // Obtener el uid del usuario autenticado
                val principal = call.principal<FirebasePrincipal>()
                val uid = principal?.decodedToken?.uid

                if (uid != null) {
                    try {
                        val clienteDto = call.receive<ClienteDTO>()
                        val cliente = clienteDto.toCliente().copy(idCliente = uid)
                        _repository.crearCliente(cliente)
                        val responseDto = cliente.toDto()

                        apiResponse.success = true
                        apiResponse.message = "Cliente creado exitosamente"
                        apiResponse.data = responseDto

                        call.respond(HttpStatusCode.Created, apiResponse)

                    } catch (e: Exception) {
                        apiResponse.success = false
                        apiResponse.message = "Error al crear cliente"
                        apiResponse.errors = listOf(e.message ?: "Error desconocido")

                        call.respond(HttpStatusCode.InternalServerError, apiResponse)
                    }

                } else {
                    apiResponse.success = false
                    apiResponse.message = "No se pudo obtener el UID del usuario autenticado"
                    apiResponse.errors = listOf("Autenticación inválida")

                    call.respond(HttpStatusCode.Unauthorized, apiResponse)
                }
            }

            //--------------------------------------------------------------------------------------------------------------

            // Obtener el cliente autenticado
            get("/obtenerCliente") {
                val apiResponse = ApiResponse<ClienteDTO>()

                // Obtener el uid del usuario autenticado
                val principal = call.principal<FirebasePrincipal>()
                val uid = principal?.decodedToken?.uid

                if (uid != null) {
                    try {
                        val cliente = _repository.obtenerClientePorId(uid)

                        if (cliente != null) {
                            val responseDto = cliente.toDto()

                            apiResponse.success = true
                            apiResponse.message = "Cliente obtenido exitosamente"
                            apiResponse.data = responseDto

                            call.respond(HttpStatusCode.OK, apiResponse)

                        } else {
                            apiResponse.success = false
                            apiResponse.message = "Cliente no encontrado"
                            apiResponse.errors = listOf("No existe un cliente con el UID proporcionado")

                            call.respond(HttpStatusCode.NotFound, apiResponse)
                        }

                    } catch (e: Exception) {
                        apiResponse.success = false
                        apiResponse.message = "Error al obtener cliente"
                        apiResponse.errors = listOf(e.message ?: "Error desconocido")

                        call.respond(HttpStatusCode.InternalServerError, apiResponse)
                    }

                } else {
                    apiResponse.success = false
                    apiResponse.message = "No se pudo obtener el UID del usuario autenticado"
                    apiResponse.errors = listOf("Autenticación inválida")

                    call.respond(HttpStatusCode.Unauthorized, apiResponse)
                }
            }

            //--------------------------------------------------------------------------------------------------------------

            // Actualizar el cliente autenticado
            put("/actualizarCliente") {
                val apiResponse = ApiResponse<Unit>()

                // Obtener el uid del usuario autenticado
                val principal = call.principal<FirebasePrincipal>()
                val uid = principal?.decodedToken?.uid

                if (uid != null) {
                    try {
                        val clienteDto = call.receive<ClienteDTO>()
                        val cliente = clienteDto.toCliente().copy(idCliente = uid)

                        val actualizado = _repository.actualizarCliente(uid, cliente)

                        if (actualizado) {
                            apiResponse.success = true
                            apiResponse.message = "Cliente actualizado"

                            call.respond(HttpStatusCode.OK, apiResponse)

                        } else {
                            apiResponse.success = false
                            apiResponse.message = "Cliente no encontrado"
                            apiResponse.errors = listOf("No existe un cliente con el UID proporcionado")

                            call.respond(HttpStatusCode.NotFound, apiResponse)
                        }

                    } catch (e: Exception) {
                        apiResponse.success = false
                        apiResponse.message = "Error al actualizar el cliente"
                        apiResponse.errors = listOf(e.message ?: "Error desconocido")

                        call.respond(HttpStatusCode.InternalServerError, apiResponse)
                    }

                } else {
                    apiResponse.success = false
                    apiResponse.message = "No se pudo obtener el UID del usuario autenticado"
                    apiResponse.errors = listOf("Autenticación inválida")

                    call.respond(HttpStatusCode.Unauthorized, apiResponse)
                }
            }

            //--------------------------------------------------------------------------------------------------------------

            // Eliminar el cliente autenticado
            delete("/eliminarCliente") {
                val apiResponse = ApiResponse<Unit>()

                // Obtener el uid del usuario autenticado
                val principal = call.principal<FirebasePrincipal>()
                val uid = principal?.decodedToken?.uid

                if (uid != null) {
                    try {
                        val eliminado = _repository.eliminarCliente(uid)

                        if (eliminado) {
                            apiResponse.success = true
                            apiResponse.message = "Cliente eliminado"

                            call.respond(HttpStatusCode.OK, apiResponse)

                        } else {
                            apiResponse.success = false
                            apiResponse.message = "Cliente no encontrado"
                            apiResponse.errors = listOf("No existe un cliente con el UID proporcionado")

                            call.respond(HttpStatusCode.NotFound, apiResponse)
                        }

                    } catch (e: Exception) {
                        apiResponse.success = false
                        apiResponse.message = "Error al eliminar el cliente"
                        apiResponse.errors = listOf(e.message ?: "Error desconocido")

                        call.respond(HttpStatusCode.InternalServerError, apiResponse)
                    }

                } else {
                    apiResponse.success = false
                    apiResponse.message = "No se pudo obtener el UID del usuario autenticado"
                    apiResponse.errors = listOf("Autenticación inválida")

                    call.respond(HttpStatusCode.Unauthorized, apiResponse)
                }
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


