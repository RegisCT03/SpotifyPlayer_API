package com.example.domain.port

import com.example.domain.model.*
import java.util.UUID

interface ArtistaRepository {
    suspend fun create(artista: Artista): Artista
    suspend fun findById(id: UUID): Artista?
    suspend fun findAll(): List<Artista>
    suspend fun update(id: UUID, updates: Map<String, Any?>): Artista?
    suspend fun delete(id: UUID): Boolean
    suspend fun findWithRelations(id: UUID): Pair<Artista, List<Pair<Album, List<Track>>>>?
}