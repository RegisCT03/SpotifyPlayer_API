import com.example.application.usecase.AlbumUseCaseImpl
import com.example.application.usecase.ArtistaUseCaseImpl
import com.example.application.usecase.TrackUseCaseImpl
import com.example.infrastructure.database.DatabaseFactory
import com.example.infrastructure.repository.AlbumRepositoryImpl
import com.example.infrastructure.repository.ArtistaRepositoryImpl
import com.example.infrastructure.repository.TrackRepositoryImpl
import infrastructure.repository.api.albumesRoutes
import infrastructure.repository.api.artistasRoutes
import infrastructure.repository.api.tracksRoutes
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

fun main() {
    embeddedServer(Netty, port = 3000, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {

    DatabaseFactory.init()

    configureSerialization()
    configureCORS()
    configureStatusPages()

    val artistaRepository = ArtistaRepositoryImpl()
    val albumRepository = AlbumRepositoryImpl()
    val trackRepository = TrackRepositoryImpl()

    val artistaUseCase = ArtistaUseCaseImpl(artistaRepository, albumRepository)
    val albumUseCase = AlbumUseCaseImpl(albumRepository, artistaRepository, trackRepository)
    val trackUseCase = TrackUseCaseImpl(trackRepository, albumRepository)

    routing {
        route("/api") {
            artistasRoutes(artistaUseCase)
            albumesRoutes(albumUseCase)
            tracksRoutes(trackUseCase)
        }

        get("/health") {
            call.respond(HttpStatusCode.OK, mapOf("status" to "UP"))
        }
    }
}

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }
}

fun Application.configureCORS() {
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        anyHost()
    }
}

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.application.log.error("Unhandled exception", cause)
            call.respond(
                HttpStatusCode.InternalServerError,
                mapOf("error" to (cause.message ?: "Error interno del servidor"))
            )
        }
    }
}