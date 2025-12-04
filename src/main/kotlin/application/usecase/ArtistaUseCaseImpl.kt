package com.example.application.usecase

import com.example.domain.model.*
import com.example.domain.port.*
import java.time.Instant
import java.util.UUID

class ArtistaUseCaseImpl(
    private val artistaRepository: ArtistaRepository,
    private val albumRepository: AlbumRepository
) : ArtistaUseCase {

    override suspend fun createArtista(request: CreateArtistaRequest): ArtistaResponse {
        val artista = Artista(
            name = request.name,
            genre = request.genre
        )
        return artistaRepository.create(artista).toResponse()
    }

    override suspend fun getArtistaById(id: String): ArtistaResponse {
        val uuid = UUID.fromString(id)
        return artistaRepository.findById(uuid)?.toResponse()
            ?: throw NotFoundException("Artista no encontrado")
    }

    override suspend fun getAllArtistas(): List<ArtistaResponse> {
        return artistaRepository.findAll().map { it.toResponse() }
    }

    override suspend fun updateArtista(id: String, request: UpdateArtistaRequest): ArtistaResponse {
        val uuid = UUID.fromString(id)

        val updates = mutableMapOf<String, Any?>()
        request.name?.let { updates["name"] = it }
        request.genre?.let { updates["genre"] = it }
        updates["updatedAt"] = Instant.now()

        return artistaRepository.update(uuid, updates)?.toResponse()
            ?: throw NotFoundException("Artista no encontrado")
    }

    override suspend fun deleteArtista(id: String): Boolean {
        val uuid = UUID.fromString(id)

        if (albumRepository.existsByArtistId(uuid)) {
            throw ConflictException(
                "No se puede eliminar el artista porque tiene álbumes asociados. " +
                        "Elimine primero los álbumes."
            )
        }

        return artistaRepository.delete(uuid)
    }

    override suspend fun getArtistaWithRelations(id: String): ArtistaWithRelationsResponse {
        val uuid = UUID.fromString(id)
        val result = artistaRepository.findWithRelations(uuid)
            ?: throw NotFoundException("Artista no encontrado")

        val (artista, albumsWithTracks) = result

        val albumesResponse = albumsWithTracks.map { (album, tracks) ->
            AlbumWithTracksResponse(
                id = album.id.toString(),
                title = album.title,
                releaseYear = album.releaseYear,
                artistId = album.artistId.toString(),
                createdAt = album.createdAt.toString(),
                updatedAt = album.updatedAt.toString(),
                tracks = tracks.map { it.toResponse() }
            )
        }

        return ArtistaWithRelationsResponse(
            id = artista.id.toString(),
            name = artista.name,
            genre = artista.genre,
            createdAt = artista.createdAt.toString(),
            updatedAt = artista.updatedAt.toString(),
            albumes = albumesResponse
        )
    }
}
