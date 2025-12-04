package com.example.infrastructure.repository

import com.example.domain.model.*
import com.example.domain.port.*
import com.example.infrastructure.database.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.UUID

class TrackRepositoryImpl : TrackRepository {

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction { block() }

    override suspend fun create(track: Track): Track = dbQuery {
        TracksTable.insert {
            it[id] = track.id
            it[title] = track.title
            it[duration] = track.duration
            it[albumId] = track.albumId
            it[createdAt] = track.createdAt
            it[updatedAt] = track.updatedAt
        }
        track
    }

    override suspend fun findById(id: UUID): Track? = dbQuery {
        TracksTable.select { TracksTable.id eq id }
            .map { it.toTrack() }
            .singleOrNull()
    }

    override suspend fun findAll(): List<Track> = dbQuery {
        TracksTable.selectAll()
            .map { it.toTrack() }
    }

    override suspend fun findByAlbumId(albumId: UUID): List<Track> = dbQuery {
        TracksTable.select { TracksTable.albumId eq albumId }
            .map { it.toTrack() }
    }

    override suspend fun update(id: UUID, updates: Map<String, Any?>): Track? = dbQuery {
        val exists = TracksTable.select { TracksTable.id eq id }.count() > 0
        if (!exists) return@dbQuery null

        TracksTable.update({ TracksTable.id eq id }) {
            updates["title"]?.let { value -> it[title] = value as String }
            updates["duration"]?.let { value -> it[duration] = value as Int }
            updates["albumId"]?.let { value -> it[albumId] = value as UUID }
            updates["updatedAt"]?.let { value -> it[updatedAt] = value as java.time.Instant }
        }

        findById(id)
    }

    override suspend fun delete(id: UUID): Boolean = dbQuery {
        TracksTable.deleteWhere { TracksTable.id eq id } > 0
    }

    override suspend fun existsByAlbumId(albumId: UUID): Boolean = dbQuery {
        TracksTable.select { TracksTable.albumId eq albumId }.count() > 0
    }

    fun ResultRow.toTrack() = Track(
        id = this[TracksTable.id],
        title = this[TracksTable.title],
        duration = this[TracksTable.duration],
        albumId = this[TracksTable.albumId],
        createdAt = this[TracksTable.createdAt],
        updatedAt = this[TracksTable.updatedAt]
    )
}