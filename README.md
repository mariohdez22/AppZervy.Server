# GUIA DE EJECUCION - APP ZERVY (SERVER)

### Instroduccion: 
Zervy (Server) es el proyecto oficial para desarrollar el apartado de la api de consumo de la app, y contendra todos los metoodos y clases necesarias para la logica del negocio.

<br>

## Lenguajes utilizados

> ### Ktor

<img src="https://codersee.com/wp-content/uploads/2023/11/ktor_refresh_token.png" alt="Logo de GitHub" width="100"/>

> ### Firebase

<img src="https://firebase.google.com/static/images/brand-guidelines/logo-vertical.png?hl=es-419" alt="Logo de GitHub" width="100"/>

<br>

## Intrucciones para ejecucion del proyecto
> [!WARNING]
> Asegurate de seguir paso a paso el instructivo, de no ser asi, podria no funcionar la compilacion del proyecto

<br>

> ### Paso 1: Agregar clave privada
Agregar la clave privada llamada "appzervy-firebase-adminsdk.json" a la zona donde se muestra en la imagen

![Descripción de la captura](resources/Multimedia/CapturaClaveSecreta.png)

<br>

> ### Paso 2: Revisar el compilador
Accedemos primero en la parte superior donde este el ApplicationKt

![Descripción de la captura](resources/Multimedia/RevisarCompilador1.png)

y luego verificamos que posea el sdk en su version 11 para el "JRE" y que el classpath este dirigido al main

![Descripción de la captura](resources/Multimedia/RevisarCompilador2.png)

<br>

> ### Paso 3: Ejecutar proyecto y probar

Comprobar el funcionamiento del proyecto, realizando peticiones en postman.
Ademas existe un archivo dentro del proyecto el cual posee el formato de las peticiones, para solo ejecutarlas rapidamente en postman
 - Esta es la ruta del archivo: src/main/kotlin/com/example/routes/Formato de consultas

<br>

## Estructura del proyecto

<br>

> ### apartado Inicial: Application.kt
En este archivo se encuentra la implementacion de todos los archivos o clases necesarias para la compilacion del proyecto

> [!WARNING]
> No tocar dicho archivo, no es necesario ya que esta correctamente configurado de manera global

``` kotlin

fun main() {
    embeddedServer(Netty, port = 8080, host = "127.0.0.1", module = Application::module)
        .start(wait = true)
}

fun Application.module() {

    install(Koin) {
        slf4jLogger()
        modules(appModule)
    }

    //configuracion de rutas
    configureRouting()

    //extras
    configureHTTP()
    configureSerialization()
}

```

<br>

> ### Primer apartado: Crear Modelo Principal (models)
En esta clase se encuentran los campos del modelo, en este caso del cliente, necesarios para realiza code first

``` kotlin

@Serializable
data class Cliente(
    val idCliente: String? = null,
    val nombres: String = "",
    val celular: String = "",
    val correo: String = "",
    val contraseña: String = "",
    val foto: String = "",
    val dui: String = ""
)

```

<br>

> ### Segundo apartado: Crear Modelo DTO (DTOs)
 En esta clase se encuentran los campos del modelo dto, en este caso del cliente, y esta clase sirve como intermediario entre el cliente y el servidor, para pasar y extraer datos de manera segura

``` kotlin

@Serializable
data class ClienteDTO (
    val idClienteDto: String? = null,
    val nombresDto: String = "",
    val celularDto: String = "",
    val correoDto: String = "",
    val contraseñaDto: String = "",
    val fotoDto: String = "",
    val duiDto: String = ""
)

```

<br>

> ### Tercer apartado: Crear proceso Mapper (Mappers)
El archivo mapper sirve para traspasar los datos provenientes del modelo dto, al modelo principal, y viceversa, mandando datos del modelo principal al dto, en este caso creamos el archivo ClienteMapper.kt para el cliente

``` kotlin

//mapper de clienteDto hacia cliente (para metodos create, update y delete)
fun ClienteDTO.toCliente(): Cliente {

    return Cliente(
        idCliente = this.idClienteDto ?: "",
        nombres = this.nombresDto,
        celular = this.celularDto,
        correo = this.correoDto,
        contraseña = this.contraseñaDto,
        foto = this.fotoDto,
        dui = this.duiDto
    )
}

//mapper de cliente hacia clienteDto (para metodos read)
fun Cliente.toDto(): ClienteDTO {

    return ClienteDTO(
        idClienteDto = this.idCliente ?: "",
        nombresDto = this.nombres,
        celularDto = this.celular,
        correoDto = this.correo,
        contraseñaDto = this.contraseña,
        fotoDto = this.foto,
        duiDto = this.dui
    )
}

```

<br>

> ### Cuarto apartado: Creando la interfaz del repository (repository/interfaz)
Se crea la interfaz IClienteRepostory del cliente, el cual nos servira como metodo de acceso, por medio de inyeccion de dependencias, para poder acceder a los metodos del crud

``` kotlin

interface IClienteRepostory {

    suspend fun crearCliente(cliente: Cliente): Cliente
    suspend fun obtenerClientes(): List<Cliente>
    suspend fun obtenerClientePorId(id: String): Cliente?
    suspend fun actualizarCliente(id: String, cliente: Cliente): Boolean
    suspend fun eliminarCliente(id: String): Boolean
}

```

<br>

> ### Quinto apartado: Creando la clase del repository (repository/clases)
Se crea la clase ClienteRepository del cliente, el cual contendra los metodos crud del cliente y una corrutina general, para realizar procesos full asincronos

``` kotlin

class ClienteRepository(private val firestore: Firestore) : IClienteRepostory {

    // Función de extensión para ApiFuture<T>
    // (puede copiarse y pegarse en todas las clases que lo necesite, ya que es metodo general)
    suspend fun <T> ApiFuture<T>.await(): T = suspendCancellableCoroutine { cont ->
        ApiFutures.addCallback(this, object : ApiFutureCallback<T> {
            override fun onSuccess(result: T) {
                if (cont.isActive) {
                    cont.resume(result)
                }
            }

            override fun onFailure(t: Throwable) {
                if (cont.isActive) {
                    cont.resumeWithException(t)
                }
            }
        }, Runnable::run)
    }

    // metodo para crear cliente
    override suspend fun crearCliente(cliente: Cliente): Cliente {

        val docRef = firestore.collection("clientes").document()
        val nuevoCliente = cliente.copy(idCliente = docRef.id)
        docRef.set(nuevoCliente).await()
        return nuevoCliente
    }

    // metodo para obtener todos los clientes
    override suspend fun obtenerClientes(): List<Cliente> {

        val snapshot = firestore.collection("clientes").get().await()
        return snapshot.documents.mapNotNull { document ->
            document.toObject(Cliente::class.java)?.copy(idCliente = document.id)
        }
    }

    // metodo para obtener un cliente por id
    override suspend fun obtenerClientePorId(id: String): Cliente? {

        val doc = firestore.collection("clientes").document(id).get().await()
        return if (doc.exists()) {
            doc.toObject(Cliente::class.java)?.copy(idCliente = doc.id)
        } else {
            null
        }
    }

    // metodo para actualizar cliente
    override suspend fun actualizarCliente(id: String, cliente: Cliente): Boolean {

        val docRef = firestore.collection("clientes").document(id)
        val updatedCliente = cliente.copy(idCliente = id)
        docRef.set(updatedCliente).await()
        return true
    }

    // metodo para eliminar cliente
    override suspend fun eliminarCliente(id: String): Boolean {

        firestore.collection("clientes").document(id).delete().await()
        return true
    }

}

```

<br>

> ### Sexto apartado: agregar el proveedor del repositorio en el FirebaseInicialized.kt
En este archivo se encuentra la configuracion de firebase, y la inyeccion de datos a la interfaz y clase de los modelos

``` kotlin

val appModule = module {

    // Proveedor del Firestore
    single<Firestore> {

        // Inicializar Firebase si no está inicializado
        if (FirebaseApp.getApps().isEmpty()) {
            val serviceAccount = this::class.java.getResourceAsStream("/appzervy-firebase-adminsdk.json")
                ?: throw IllegalStateException("No se pudo encontrar el archivo de credenciales de Firebase.")

            val options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setProjectId("appzervy")
                .build()

            FirebaseApp.initializeApp(options)
        }
        FirestoreClient.getFirestore()
    }

    // Proveedor de ClienteRepository
    single<IClienteRepostory> { ClienteRepository(get()) }

    //Proveedor de DireccionRepository
    single<IDireccionRepository> {DireccionRepository(get())}

    //Proveedor de MetodosPagoRepository
    single<IMetodosPagoRepository> {MetodosPagoRepository(get())}
}

```

<br>

> ### Septimo apartado: creando el archivo clienteRoute (route)
creamos solamente el archivo y configuramos la funcion de ruta interna (clienteRouting)

``` kotlin

fun Route.clienteRouting(_repository: IClienteRepostory) {

}

```

<br>

> ### Octavo apartado: Routing.kt (plugins)
En este archivo, se encuentran las rutas las cuales luego se mandan a llamar por medio de la instancia "configureRouting()" en el "Application.kt"
Ademas en este archivo se pueden implementar los metodos rest, para la ejecucion del crud, pero lo haremos de manera mas ordenada, inyectando el repositorio y pasandolo al archivo clienteRouting.kt

``` kotlin

fun Application.configureRouting() {

    val _repositoryCliente by inject<IClienteRepostory>()

    val _repositoryDireccion by inject<IDireccionRepository>()

    val _repositoryMetodosPago by inject<IMetodosPagoRepository>()

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

    }
}

```

<br>

> ### Noveno apartado: volvemos a ClienteRoute.kt para configurar los procesos REST (routes)
En este archivo, se encontraran las ejecuciones de las peticiones del repositorio, exclusivamente para el modelo de clientes, basicamente este archivo funciona como un controlador, ya que en ktor no se les llama controllers, si no rutas.
Ademas el archivo pide el repositorio como parametro, el cual hemos mandado desde Routing.kt

``` kotlin

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

```

<br>

> ### Apartado Extra: ApiResponse.kt (ApiResponse)
Esta clase contiene los campos para mostrar errores o estados dependiendo de la resolucion de las peticiones rest, y pueden verlo instanciado en los metodos rest de la funcion "clienteRouting" en clienteRoute (routes)

> [!WARNING]
> No modificar por nada del mundo este archivo, ya esta configurado de manera global, solo para mandarlo a llamar en los diferentes modelos

``` kotlin

@Serializable
data class ApiResponse<T>(
    var success: Boolean = false,
    var message: String = "",
    var data: T? = null,
    var errors: List<String>? = null
)
```

<br>

## Conclusiones del proyecto
Con todo lo enseñado en este archivo, es mas que suficiente para comprender el funcionamiento y realizacion de una api basica en ktor con firebase










