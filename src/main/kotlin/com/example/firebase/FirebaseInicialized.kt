package com.example.firebase

import com.example.repository.clases.ClienteRepository
import com.example.repository.clases.DireccionRepository
import com.example.repository.clases.MetodosPagoRepository
import com.example.repository.interfaces.IClienteRepostory
import com.example.repository.interfaces.IDireccionRepository
import com.example.repository.interfaces.IMetodosPagoRepository
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.cloud.firestore.Firestore
import com.google.firebase.cloud.FirestoreClient
import org.koin.dsl.module

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

    // Proveedor de PersonalRepository
    single< IPersonalRepository> { PersonalRepository(get())}

    // Proveedor de ReseñasRepository
    single< IReseñasRepository> { ReseñasRepository(get())}

    // Proveedor de EvidenciaServicioRepository
    single< IEvidenciaServicioRepository> { EvidenciaServicioRepository(get())}

    //Proveedor de CategoriaServicioRepository
    single<ICategoriaServicioRepository> {CategoriaServicioRepository(get())}

    //Proveedor de EjecucionServiceRepository
    single<IEjecucionServicioRepository> {EjecucionServiceRepository(get())}

    //Proveedor de InspeccionRepository
    single<IInspeccionRepository> {InspeccionRepository(get())}

    //Proveedor de PropuestaServicioRepository
    single<IPropuestaServicioRepository> {PropuestaServicioRepository(get())}

    single<IPagoServicioRepository> {PagoServicioRepository(get())}

    single<IPropuestaServicioRepository> {PropuestaServicioRepository(get())}

    single<IEjecucionServicioRepository> {EjecucionServiceRepository(get())}

    single<IInspeccionRepository> {InspeccionRepository(get())}

    single<IDetallePagoRepository> {DetallePagoRepository(get())}

}