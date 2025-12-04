package com.example.domain.port

import com.example.domain.model.ArtistaResponse
import com.example.domain.model.ArtistaWithRelationsResponse
import com.example.domain.model.CreateArtistaRequest
import com.example.domain.model.UpdateArtistaRequest

interface ArtistaUseCase {
    suspend fun createArtista(request: CreateArtistaRequest): ArtistaResponse
    suspend fun getArtistaById(id: String): ArtistaResponse
    suspend fun getAllArtistas(): List<ArtistaResponse>
    suspend fun updateArtista(id: String, request: UpdateArtistaRequest): ArtistaResponse
    suspend fun deleteArtista(id: String): Boolean
    suspend fun getArtistaWithRelations(id: String): ArtistaWithRelationsResponse
}