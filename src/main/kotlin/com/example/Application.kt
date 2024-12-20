package com.example

import com.example.firebase.*
import com.example.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun main() {
    embeddedServer(Netty, port = 8080, host = "127.0.0.1", module = Application::module)
        .start(wait = true)
}

fun Application.module() {

    install(Koin) {
        slf4jLogger()
        modules(appModule)
    }

    // Configurar autenticación
    configureAuthentication()


    //configuracion de rutas
    configureRouting()

    //extras
    configureHTTP()
    configureSerialization()
}

