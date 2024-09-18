package com.example.plugins

import com.example.repository.interfaces.IClienteRepostory
import com.example.routes.clienteRouting
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {

    val _repository by inject<IClienteRepostory>()

    routing {

        get("/") {
            call.respondText("Vale verga este texto xd")
        }

        //llamada de funcion(controlador) del cliente
        clienteRouting(_repository)

    }
}
