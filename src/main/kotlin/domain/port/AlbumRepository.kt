package com.example.domain.port

import com.example.domain.model.Album
import java.util.UUID

interface AlbumRepository {
    suspend fun create(album: Album): Album
    suspend fun findById(id: UUID): Album?
    suspend fun findAll(): List<Album>
    suspend fun findByArtistId(artistId: UUID): List<Album>
    suspend fun update(id: UUID, updates: Map<String, Any?>): Album?
    suspend fun delete(id: UUID): Boolean
    suspend fun existsByArtistId(artistId: UUID): Boolean
}