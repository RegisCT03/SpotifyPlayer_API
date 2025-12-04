package com.example.domain.model

fun Artista.toResponse() = ArtistaResponse(
    id = id.toString(),
    name = name,
    genre = genre,
    createdAt = createdAt.toString(),
    updatedAt = updatedAt.toString()
)

fun Album.toResponse() = AlbumResponse(
    id = id.toString(),
    title = title,
    releaseYear = releaseYear,
    artistId = artistId.toString(),
    createdAt = createdAt.toString(),
    updatedAt = updatedAt.toString()
)

fun Track.toResponse() = TrackResponse(
    id = id.toString(),
    title = title,
    duration = duration,
    albumId = albumId.toString(),
    createdAt = createdAt.toString(),
    updatedAt = updatedAt.toString()
)