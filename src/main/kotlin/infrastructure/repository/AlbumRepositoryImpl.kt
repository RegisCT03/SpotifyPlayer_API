package com.example.infrastructure.repository

import com.example.domain.model.*
import com.example.domain.port.*
import com.example.infrastructure.database.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.UUID

class AlbumRepositoryImpl : AlbumRepository {

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction { block() }

    override suspend fun create(album: Album): Album = dbQuery {
        AlbumesTable.insert {
            it[id] = album.id
            it[title] = album.title
            it[releaseYear] = album.releaseYear
            it[artistId] = album.artistId
            it[createdAt] = album.createdAt
            it[updatedAt] = album.updatedAt
        }
        album
    }

    override suspend fun findById(id: UUID): Album? = dbQuery {
        AlbumesTable.select { AlbumesTable.id eq id }
            .map { it.toAlbum() }
            .singleOrNull()
    }

    override suspend fun findAll(): List<Album> = dbQuery {
        AlbumesTable.selectAll()
            .map { it.toAlbum() }
    }

    override suspend fun findByArtistId(artistId: UUID): List<Album> = dbQuery {
        AlbumesTable.select { AlbumesTable.artistId eq artistId }
            .map { it.toAlbum() }
    }

    override suspend fun update(id: UUID, updates: Map<String, Any?>): Album? = dbQuery {
        val exists = AlbumesTable.select { AlbumesTable.id eq id }.count() > 0
        if (!exists) return@dbQuery null

        AlbumesTable.update({ AlbumesTable.id eq id }) {
            updates["title"]?.let { value -> it[title] = value as String }
            updates["releaseYear"]?.let { value -> it[releaseYear] = value as Int }
            updates["artistId"]?.let { value -> it[artistId] = value as UUID }
            updates["updatedAt"]?.let { value -> it[updatedAt] = value as java.time.Instant }
        }

        findById(id)
    }

    override suspend fun delete(id: UUID): Boolean = dbQuery {
        AlbumesTable.deleteWhere { AlbumesTable.id eq id } > 0
    }

    override suspend fun existsByArtistId(artistId: UUID): Boolean = dbQuery {
        AlbumesTable.select { AlbumesTable.artistId eq artistId }.count() > 0
    }

    private fun ResultRow.toAlbum() = Album(
        id = this[AlbumesTable.id],
        title = this[AlbumesTable.title],
        releaseYear = this[AlbumesTable.releaseYear],
        artistId = this[AlbumesTable.artistId],
        createdAt = this[AlbumesTable.createdAt],
        updatedAt = this[AlbumesTable.updatedAt]
    )
}