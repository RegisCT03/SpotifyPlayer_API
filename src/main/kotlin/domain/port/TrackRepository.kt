package com.example.domain.port

import com.example.domain.model.Track
import java.util.UUID

interface TrackRepository {
    suspend fun create(track: Track): Track
    suspend fun findById(id: UUID): Track?
    suspend fun findAll(): List<Track>
    suspend fun findByAlbumId(albumId: UUID): List<Track>
    suspend fun update(id: UUID, updates: Map<String, Any?>): Track?
    suspend fun delete(id: UUID): Boolean
    suspend fun existsByAlbumId(albumId: UUID): Boolean
}