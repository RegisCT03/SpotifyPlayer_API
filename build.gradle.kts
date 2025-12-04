plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    id("com.gradleup.shadow") version "9.2.2"
    kotlin("plugin.serialization") version "1.9.22"
}

group = "com.example"
version = "0.0.1"

val ktor_version = "3.3.2"

application {
    mainClass = "com.example.ApplicationKt"
}

tasks {
    shadowJar {
        // Esto evita conflictos con nombres de archivos duplicados en librerías
        mergeServiceFiles()
        manifest {
            attributes(mapOf("Main-Class" to "com.example.ApplicationKt"))
        }
        // Opcional: Nombre fijo para no tener versiones en el nombre del archivo
        archiveFileName.set("mi-api2.jar")
    }
}

dependencies {
    // Ktor Server
    implementation("io.ktor:ktor-server-core:2.3.7")
    implementation("io.ktor:ktor-server-netty:2.3.7")
    implementation("io.ktor:ktor-server-content-negotiation:2.3.7")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.7")
    implementation("io.ktor:ktor-server-status-pages:2.3.7")
    implementation("io.ktor:ktor-server-cors:2.3.7")
    implementation("io.ktor:ktor-server-call-logging:2.3.7")

    // PostgreSQL
    implementation("org.postgresql:postgresql:42.7.1")
    implementation("org.jetbrains.exposed:exposed-core:0.45.0")
    implementation("org.jetbrains.exposed:exposed-dao:0.45.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.45.0")
    implementation("org.jetbrains.exposed:exposed-java-time:0.45.0")
    implementation("com.zaxxer:HikariCP:5.1.0")

    // Logging
    implementation("ch.qos.logback:logback-classic:1.4.14")

    // Kotlinx Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")

    // Testing
    testImplementation("io.ktor:ktor-server-test-host:2.3.7")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.9.22")
}

application {
    mainClass.set("com.example.ApplicationKt")
}

kotlin {
    jvmToolchain(17)
}