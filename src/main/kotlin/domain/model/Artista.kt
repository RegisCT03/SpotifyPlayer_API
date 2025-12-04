package com.example.domain.model

import java.time.Instant
import java.util.UUID

data class Artista(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val genre: String?,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now()
)