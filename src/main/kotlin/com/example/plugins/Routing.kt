package com.example.plugins

import com.example.repository.clases.DetallePagoRepository
import com.example.repository.interfaces.*
import com.example.routes.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {

    val _repositoryCliente by inject<IClienteRepostory>()

    val _repositoryDireccion by inject<IDireccionRepository>()

    val _repositoryMetodosPago by inject<IMetodosPagoRepository>()

    val _repositoryCategoriaServicio by inject<ICategoriaServicioRepository>()

    val _repositoryPropuestaServicio by inject<IPropuestaServicioRepository>()

    val _repositoryInspeccion by inject<IInspeccionRepository>()

    val _repositoryEjecucionServicio by inject<IEjecucionServicioRepository>()

    val _repositoryPagoServicio by inject<IPagoServicioRepository>()

    val _repositoryDetallePago by inject<DetallePagoRepository>()

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

        //Categoria servicio
        categoriaServicioRouting(_repositoryCategoriaServicio)

        //Propuesta Servicio
        PropuestaServicioRouting(_repositoryPropuestaServicio)

        InspeccionRouting(_repositoryInspeccion)

        EjecucionServicioRouting(_repositoryEjecucionServicio)

        PagoServicioRouting(_repositoryPagoServicio)

        DetallePagoRouting(_repositoryDetallePago)
    }
}
