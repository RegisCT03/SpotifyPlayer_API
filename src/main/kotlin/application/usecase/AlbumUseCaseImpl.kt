package com.example.application.usecase

import com.example.domain.model.*
import com.example.domain.port.*
import java.time.Instant
import java.util.UUID

class AlbumUseCaseImpl(
    private val albumRepository: AlbumRepository,
    private val artistaRepository: ArtistaRepository,
    private val trackRepository: TrackRepository
) : AlbumUseCase {

    override suspend fun createAlbum(request: CreateAlbumRequest): AlbumResponse {
        val artistId = UUID.fromString(request.artistId)

        artistaRepository.findById(artistId)
            ?: throw NotFoundException("Artista no encontrado")

        val album = Album(
            title = request.title,
            releaseYear = request.releaseYear,
            artistId = artistId
        )

        return albumRepository.create(album).toResponse()
    }

    override suspend fun getAlbumById(id: String): AlbumResponse {
        val uuid = UUID.fromString(id)
        return albumRepository.findById(uuid)?.toResponse()
            ?: throw NotFoundException("Álbum no encontrado")
    }

    override suspend fun getAllAlbumes(): List<AlbumResponse> {
        return albumRepository.findAll().map { it.toResponse() }
    }

    override suspend fun updateAlbum(id: String, request: UpdateAlbumRequest): AlbumResponse {
        val uuid = UUID.fromString(id)

        val updates = mutableMapOf<String, Any?>()
        request.title?.let { updates["title"] = it }
        request.releaseYear?.let { updates["releaseYear"] = it }
        request.artistId?.let {
            val artistId = UUID.fromString(it)
            artistaRepository.findById(artistId)
                ?: throw NotFoundException("Artista no encontrado")
            updates["artistId"] = artistId
        }
        updates["updatedAt"] = Instant.now()

        return albumRepository.update(uuid, updates)?.toResponse()
            ?: throw NotFoundException("Álbum no encontrado")
    }

    override suspend fun deleteAlbum(id: String): Boolean {
        val uuid = UUID.fromString(id)

        if (trackRepository.existsByAlbumId(uuid)) {
            throw ConflictException(
                "No se puede eliminar el álbum porque tiene tracks asociados. " +
                        "Elimine primero los tracks."
            )
        }

        return albumRepository.delete(uuid)
    }
}