package com.example.plugins

import com.example.repository.interfaces.IClienteRepostory
import com.example.repository.interfaces.IDireccionRepository
import com.example.repository.interfaces.IMetodosPagoRepository
import com.example.repository.interfaces.IPropuestaServicioRepository
import com.example.routes.PropuestaServicio
import com.example.routes.clienteRouting
import com.example.routes.direccionRouting
import com.example.routes.metodosPagoRouting
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {

    val _repositoryCliente by inject<IClienteRepostory>()

    val _repositoryDireccion by inject<IDireccionRepository>()

    val _repositoryMetodosPago by inject<IMetodosPagoRepository>()

    val _repositoryPropuestaServicio by inject<IPropuestaServicioRepository>()

    routing {

        get("/") {
            call.respondText("Vale verga este texto xd")
        }

        //llamada de funcion(controlador) del cliente
        clienteRouting(_repositoryCliente)

        //llamada de funcion(controlador) de direccion
        direccionRouting(_repositoryDireccion)

        //llamada de funcion(controlador) de metodosPago
        metodosPagoRouting(_repositoryMetodosPago)

        //Propuesta Servicio
        PropuestaServicio(_repositoryPropuestaServicio)
    }
}
