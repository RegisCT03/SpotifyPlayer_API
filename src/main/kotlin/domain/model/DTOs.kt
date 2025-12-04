package com.example.domain.model

import kotlinx.serialization.Serializable

//Requests
@Serializable
data class CreateArtistaRequest(
    val name: String,
    val genre: String? = null
)

@Serializable
data class UpdateArtistaRequest(
    val name: String? = null,
    val genre: String? = null
)

@Serializable
data class CreateAlbumRequest(
    val title: String,
    val releaseYear: Int,
    val artistId: String
)
@Serializable
data class UpdateAlbumRequest(
    val title: String? = null,
    val releaseYear: Int? = null,
    val artistId: String? = null
)

@Serializable
data class CreateTrackRequest(
    val title: String,
    val duration: Int,
    val albumId: String
)

@Serializable
data class UpdateTrackRequest(
    val title: String? = null,
    val duration: Int? = null,
    val albumId: String? = null
)

//Responses
@Serializable
data class ArtistaResponse(
    val id: String,
    val name: String,
    val genre: String?,
    val createdAt: String,
    val updatedAt: String
)

@Serializable
data class AlbumResponse(
    val id: String,
    val title: String,
    val releaseYear: Int,
    val artistId: String,
    val createdAt: String,
    val updatedAt: String
)

@Serializable
data class TrackResponse(
    val id: String,
    val title: String,
    val duration: Int,
    val albumId: String,
    val createdAt: String,
    val updatedAt: String
)
@Serializable
data class ArtistaWithRelationsResponse(
    val id: String,
    val name: String,
    val genre: String?,
    val createdAt: String,
    val updatedAt: String,
    val albumes: List<AlbumWithTracksResponse> = emptyList()
)

@Serializable
data class AlbumWithTracksResponse(
    val id: String,
    val title: String,
    val releaseYear: Int,
    val artistId: String,
    val createdAt: String,
    val updatedAt: String,
    val tracks: List<TrackResponse> = emptyList()
)