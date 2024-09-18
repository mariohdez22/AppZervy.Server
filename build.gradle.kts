val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "2.0.20"
    id("io.ktor.plugin") version "3.0.0-rc-1"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.20"
}

group = "com.example"
version = "0.0.1"

application {
    mainClass.set("com.example.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-openapi")
    implementation("io.ktor:ktor-server-swagger-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-server-cors-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")

    //implementacion firebase
    implementation("com.google.firebase:firebase-admin:9.3.0")

    //implementacion corrutinas
    //implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")

    //implementacion corritinas core
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")

    //implementacion koin (inyeccion de dependencias)
    implementation("io.insert-koin:koin-ktor:3.5.6")

    //implementacion logger koin
    implementation("io.insert-koin:koin-logger-slf4j:3.5.6")

    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-test-host-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}
