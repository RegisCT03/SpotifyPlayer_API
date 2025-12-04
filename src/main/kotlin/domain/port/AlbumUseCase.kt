package com.example.domain.port

import com.example.domain.model.AlbumResponse
import com.example.domain.model.CreateAlbumRequest
import com.example.domain.model.UpdateAlbumRequest

interface AlbumUseCase {
    suspend fun createAlbum(request: CreateAlbumRequest): AlbumResponse
    suspend fun getAlbumById(id: String): AlbumResponse
    suspend fun getAllAlbumes(): List<AlbumResponse>
    suspend fun updateAlbum(id: String, request: UpdateAlbumRequest): AlbumResponse
    suspend fun deleteAlbum(id: String): Boolean
}