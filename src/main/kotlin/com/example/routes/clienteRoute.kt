package com.example.routes

import com.example.models.Cliente
import com.example.repository.interfaces.IClienteRepostory
import com.google.firebase.cloud.FirestoreClient
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.clienteRouting(_repository: IClienteRepostory) {

    route("/clientes") {

        // Crear un nuevo cliente
        post("/crearCliente") {

            val cliente = call.receive<Cliente>()
            val nuevoCliente = _repository.crearCliente(cliente)
            call.respond(HttpStatusCode.Created, nuevoCliente)
        }

        // Obtener todos los clientes
        get("/obtenerClientes") {

            val obtenerClientes = _repository.obtenerClientes()
            call.respond(obtenerClientes)
        }

        // Obtener un cliente por ID
        get("/obtenerClienteId/{id}") {

            val id = call.parameters["id"]

            if (id != null) {

                val cliente = _repository.obtenerClientePorId(id)

                if (cliente != null) {
                    call.respond(cliente)
                } else {
                    call.respond(HttpStatusCode.NotFound, "Cliente no encontrado")
                }

            } else {
                call.respond(HttpStatusCode.BadRequest, "ID de cliente no proporcionado")
            }
        }

        // Actualizar un cliente
        put("/actualizarCliente") {

            val cliente = call.receive<Cliente>()

            val id = cliente.idCliente

            if (id != null) {

                val actualizado = _repository.actualizarCliente(id, cliente)

                if (actualizado) {
                    call.respond(HttpStatusCode.OK, "Cliente actualizado")
                } else {
                    call.respond(HttpStatusCode.NotFound, "Cliente no encontrado")
                }

            } else {
                call.respond(HttpStatusCode.BadRequest, "ID de cliente no proporcionado")
            }
        }

        // Eliminar un cliente
        delete("/eliminarCiente") {

            val data = call.receive<Map<String, String>>()

            val id = data["idCliente"]

            if (id != null) {

                val eliminado = _repository.eliminarCliente(id)

                if (eliminado) {
                    call.respond(HttpStatusCode.OK, "Cliente eliminado")
                } else {
                    call.respond(HttpStatusCode.NotFound, "Cliente no encontrado")
                }

            } else {
                call.respond(HttpStatusCode.BadRequest, "ID de cliente no proporcionado")
            }
        }
    }
}

//----------------------------------------------------------------------------------------------------------------------

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



