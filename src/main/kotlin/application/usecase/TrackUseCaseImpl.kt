package com.example.application.usecase

import com.example.domain.model.*
import com.example.domain.port.*
import java.time.Instant
import java.util.UUID

class TrackUseCaseImpl(
    private val trackRepository: TrackRepository,
    private val albumRepository: AlbumRepository
) : TrackUseCase {

    override suspend fun createTrack(request: CreateTrackRequest): TrackResponse {
        val albumId = UUID.fromString(request.albumId)

        albumRepository.findById(albumId)
            ?: throw NotFoundException("Álbum no encontrado")

        val track = Track(
            title = request.title,
            duration = request.duration,
            albumId = albumId
        )

        return trackRepository.create(track).toResponse()
    }

    override suspend fun getTrackById(id: String): TrackResponse {
        val uuid = UUID.fromString(id)
        return trackRepository.findById(uuid)?.toResponse()
            ?: throw NotFoundException("Track no encontrado")
    }

    override suspend fun getAllTracks(): List<TrackResponse> {
        return trackRepository.findAll().map { it.toResponse() }
    }

    override suspend fun updateTrack(id: String, request: UpdateTrackRequest): TrackResponse {
        val uuid = UUID.fromString(id)

        val updates = mutableMapOf<String, Any?>()
        request.title?.let { updates["title"] = it }
        request.duration?.let { updates["duration"] = it }
        request.albumId?.let {
            val albumId = UUID.fromString(it)
            albumRepository.findById(albumId)
                ?: throw NotFoundException("Álbum no encontrado")
            updates["albumId"] = albumId
        }
        updates["updatedAt"] = Instant.now()

        return trackRepository.update(uuid, updates)?.toResponse()
            ?: throw NotFoundException("Track no encontrado")
    }

    override suspend fun deleteTrack(id: String): Boolean {
        val uuid = UUID.fromString(id)
        return trackRepository.delete(uuid)
    }
}