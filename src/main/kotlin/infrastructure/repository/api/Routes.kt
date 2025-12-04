package infrastructure.repository.api

import com.example.application.usecase.*
import com.example.domain.model.*
import com.example.domain.port.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

//Artistas
fun Route.artistasRoutes(useCase: ArtistaUseCase) {

    route("/artistas") {

        post {
            try {
                val request = call.receive<CreateArtistaRequest>()
                val response = useCase.createArtista(request)
                call.respond(HttpStatusCode.Created, response)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message ?: "Error desconocido"))
            }
        }

        get {
            try {
                val artistas = useCase.getAllArtistas()
                call.respond(HttpStatusCode.OK, artistas)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, ErrorResponse(e.message ?: "Error desconocido"))
            }
        }

        get("/{id}") {
            try {
                val id = call.parameters["id"] ?: throw IllegalArgumentException("ID inválido")
                val artista = useCase.getArtistaById(id)
                call.respond(HttpStatusCode.OK, artista)
            } catch (e: NotFoundException) {
                call.respond(HttpStatusCode.NotFound, ErrorResponse(e.message ?: "No encontrado"))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message ?: "Error desconocido"))
            }
        }

        get("/{id}/relations") {
            try {
                val id = call.parameters["id"] ?: throw IllegalArgumentException("ID inválido")
                val artista = useCase.getArtistaWithRelations(id)
                call.respond(HttpStatusCode.OK, artista)
            } catch (e: NotFoundException) {
                call.respond(HttpStatusCode.NotFound, ErrorResponse(e.message ?: "No encontrado"))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message ?: "Error desconocido"))
            }
        }

        put("/{id}") {
            try {
                val id = call.parameters["id"] ?: throw IllegalArgumentException("ID inválido")
                val request = call.receive<UpdateArtistaRequest>()
                val response = useCase.updateArtista(id, request)
                call.respond(HttpStatusCode.OK, response)
            } catch (e: NotFoundException) {
                call.respond(HttpStatusCode.NotFound, ErrorResponse(e.message ?: "No encontrado"))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message ?: "Error desconocido"))
            }
        }

        delete("/{id}") {
            try {
                val id = call.parameters["id"] ?: throw IllegalArgumentException("ID inválido")
                val deleted = useCase.deleteArtista(id)
                if (deleted) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.NotFound, ErrorResponse("Artista no encontrado"))
                }
            } catch (e: ConflictException) {
                call.respond(HttpStatusCode.Conflict, ErrorResponse(e.message ?: "Conflicto"))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message ?: "Error desconocido"))
            }
        }
    }
}

//Album

fun Route.albumesRoutes(useCase: AlbumUseCase) {

    route("/albumes") {

        post {
            try {
                val request = call.receive<CreateAlbumRequest>()
                val response = useCase.createAlbum(request)
                call.respond(HttpStatusCode.Created, response)
            } catch (e: NotFoundException) {
                call.respond(HttpStatusCode.NotFound, ErrorResponse(e.message ?: "No encontrado"))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message ?: "Error desconocido"))
            }
        }

        get {
            try {
                val albumes = useCase.getAllAlbumes()
                call.respond(HttpStatusCode.OK, albumes)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, ErrorResponse(e.message ?: "Error desconocido"))
            }
        }

        get("/{id}") {
            try {
                val id = call.parameters["id"] ?: throw IllegalArgumentException("ID inválido")
                val album = useCase.getAlbumById(id)
                call.respond(HttpStatusCode.OK, album)
            } catch (e: NotFoundException) {
                call.respond(HttpStatusCode.NotFound, ErrorResponse(e.message ?: "No encontrado"))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message ?: "Error desconocido"))
            }
        }

        put("/{id}") {
            try {
                val id = call.parameters["id"] ?: throw IllegalArgumentException("ID inválido")
                val request = call.receive<UpdateAlbumRequest>()
                val response = useCase.updateAlbum(id, request)
                call.respond(HttpStatusCode.OK, response)
            } catch (e: NotFoundException) {
                call.respond(HttpStatusCode.NotFound, ErrorResponse(e.message ?: "No encontrado"))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message ?: "Error desconocido"))
            }
        }

        delete("/{id}") {
            try {
                val id = call.parameters["id"] ?: throw IllegalArgumentException("ID inválido")
                val deleted = useCase.deleteAlbum(id)
                if (deleted) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.NotFound, ErrorResponse("Álbum no encontrado"))
                }
            } catch (e: ConflictException) {
                call.respond(HttpStatusCode.Conflict, ErrorResponse(e.message ?: "Conflicto"))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message ?: "Error desconocido"))
            }
        }
    }
}

//Track

fun Route.tracksRoutes(useCase: TrackUseCase) {

    route("/tracks") {

        post {
            try {
                val request = call.receive<CreateTrackRequest>()
                val response = useCase.createTrack(request)
                call.respond(HttpStatusCode.Created, response)
            } catch (e: NotFoundException) {
                call.respond(HttpStatusCode.NotFound, ErrorResponse(e.message ?: "No encontrado"))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message ?: "Error desconocido"))
            }
        }

        get {
            try {
                val tracks = useCase.getAllTracks()
                call.respond(HttpStatusCode.OK, tracks)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, ErrorResponse(e.message ?: "Error desconocido"))
            }
        }

        get("/{id}") {
            try {
                val id = call.parameters["id"] ?: throw IllegalArgumentException("ID inválido")
                val track = useCase.getTrackById(id)
                call.respond(HttpStatusCode.OK, track)
            } catch (e: NotFoundException) {
                call.respond(HttpStatusCode.NotFound, ErrorResponse(e.message ?: "No encontrado"))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message ?: "Error desconocido"))
            }
        }

        put("/{id}") {
            try {
                val id = call.parameters["id"] ?: throw IllegalArgumentException("ID inválido")
                val request = call.receive<UpdateTrackRequest>()
                val response = useCase.updateTrack(id, request)
                call.respond(HttpStatusCode.OK, response)
            } catch (e: NotFoundException) {
                call.respond(HttpStatusCode.NotFound, ErrorResponse(e.message ?: "No encontrado"))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message ?: "Error desconocido"))
            }
        }

        delete("/{id}") {
            try {
                val id = call.parameters["id"] ?: throw IllegalArgumentException("ID inválido")
                val deleted = useCase.deleteTrack(id)
                if (deleted) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.NotFound, ErrorResponse("Track no encontrado"))
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message ?: "Error desconocido"))
            }
        }
    }
}

@Serializable
data class ErrorResponse(val error: String)