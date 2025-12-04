package com.example.domain.port

import com.example.domain.model.CreateTrackRequest
import com.example.domain.model.TrackResponse
import com.example.domain.model.UpdateTrackRequest

interface TrackUseCase {
    suspend fun createTrack(request: CreateTrackRequest): TrackResponse
    suspend fun getTrackById(id: String): TrackResponse
    suspend fun getAllTracks(): List<TrackResponse>
    suspend fun updateTrack(id: String, request: UpdateTrackRequest): TrackResponse
    suspend fun deleteTrack(id: String): Boolean
}