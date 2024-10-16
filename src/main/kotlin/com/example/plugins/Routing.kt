package com.example.plugins

import com.example.repository.clases.DetallePagoRepository
import com.example.models.PropuestaServicio
import com.example.repository.clases.FotoSolicitudRepository
import com.example.repository.interfaces.*
import com.example.routes.*
import com.example.repository.interfaces.IClienteRepostory
import com.example.repository.interfaces.IDireccionRepository
import com.example.repository.interfaces.IEvidenciaServicioRepository
import com.example.repository.interfaces.IMetodosPagoRepository
import com.example.repository.interfaces.IPersonalRepository
import com.example.repository.interfaces.IPropuestaServicioRepository
import com.example.repository.interfaces.IReseñasRepository
import com.example.routes.PropuestaServicioRouting
import com.example.routes.clienteRouting
import com.example.routes.direccionRouting
import com.example.routes.evidenciaServicioRouting
import com.example.routes.metodosPagoRouting
import com.example.routes.personalRouting
import com.example.routes.reseñasRouting
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

    val _repositoryIntegrante by inject<IIntegranteRepository>()

    val _repositorySocioComercial by inject<ISocioComercialRepository>()

    val _repositorySocioIndividual by inject<ISocioIndividualRepository>()

    val _repositorySocio by inject<ISocioRepository>()

    val _repositoryPersonal by inject<IPersonalRepository>()

    val _repositoryReseñas by inject<IReseñasRepository>()

    val _repositoryEvidenciaServicio by inject<IEvidenciaServicioRepository>()

    val _repositoryInspeccion by inject<IInspeccionRepository>()

    val _repositoryEjecucionServicio by inject<IEjecucionServicioRepository>()

    val _repositoryPagoServicio by inject<IPagoServicioRepository>()

    val _repositoryDetallePago by inject<IDetallePagoRepository>()

    val _repositoryFotoSolicitud by inject<IFotoSolicitudRepository>()

    val _repositoryPagoSocio by inject<IPagoSocioRepository>()

    val _repositorySolicitudServicio by inject<ISolicitudServicioRepository>()

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
        //PropuestaServicio(_repositoryPropuestaServicio)

        //Servicio Integrante
        integranteRouting(_repositoryIntegrante)

        //Servicio Socio Comercial
        socioComercialRoute(_repositorySocioComercial)

        //Servicio Socio Individual
        socioIndividualRouting(_repositorySocioIndividual)

        //Servicio Socio
        socioRouting(_repositorySocio)

        //llamada de funcion(controlador) del Personal
        personalRouting(_repositoryPersonal)

        //llamada de funcion(controlador) de Reseñas
        reseñasRouting(_repositoryReseñas)

        //llamada de funcion(controlador) del Personal
        evidenciaServicioRouting(_repositoryEvidenciaServicio)

        //a
        PropuestaServicioRouting(_repositoryPropuestaServicio)

        //e
        InspeccionRouting(_repositoryInspeccion)

        //i
        EjecucionServicioRouting(_repositoryEjecucionServicio)

        //o
        PagoServicioRouting(_repositoryPagoServicio)

        //u
        DetallePagoRouting(_repositoryDetallePago)

        //1
        fotoSolicitudRouting(_repositoryFotoSolicitud)

        //2
        pagoSocioRouting(_repositoryPagoSocio)

        //3
        solicitudServicioRouting(_repositorySolicitudServicio)
    }
}
