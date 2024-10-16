package com.example.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.http.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseToken
import io.ktor.http.auth.*
import io.ktor.server.request.*

fun Application.configureAuthentication() {
    install(Authentication) {
        firebase("firebase-auth")
    }
}

fun AuthenticationConfig.firebase(name: String? = null) {
    val provider = FirebaseAuthenticationProvider(name)
    register(provider)
}

class FirebaseAuthenticationProvider(
    name: String?
) : AuthenticationProvider(Config(name)) {

    override suspend fun onAuthenticate(context: AuthenticationContext) {
        val call = context.call
        val authHeader = call.request.parseAuthorizationHeader()

        if (authHeader != null && authHeader is HttpAuthHeader.Single && authHeader.authScheme == "Bearer") {
            val token = authHeader.blob
            try {
                val decodedToken = FirebaseAuth.getInstance().verifyIdToken(token)
                val principal = FirebasePrincipal(decodedToken)
                context.principal(principal)
            } catch (e: Exception) {
                // Token inválido o expirado
                context.challenge("FirebaseAuth", AuthenticationFailedCause.InvalidCredentials) { challenge, call ->
                    call.respond(HttpStatusCode.Unauthorized, "Token de autenticación inválido o expirado")
                    challenge.complete()
                }
            }
        } else {
            // No se proporcionó el token
            context.challenge("FirebaseAuth", AuthenticationFailedCause.NoCredentials) { challenge, call ->
                call.respond(HttpStatusCode.Unauthorized, "Token de autenticación no proporcionado")
                challenge.complete()
            }
        }
    }

    class Config(name: String?) : AuthenticationProvider.Config(name)
}

data class FirebasePrincipal(val decodedToken: FirebaseToken) : Principal
