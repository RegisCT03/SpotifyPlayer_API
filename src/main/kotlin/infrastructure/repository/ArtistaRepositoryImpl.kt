package com.example.infrastructure.repository

import com.example.domain.model.*
import com.example.domain.port.*
import com.example.infrastructure.database.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.UUID

class ArtistaRepositoryImpl : ArtistaRepository {

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction { block() }

    override suspend fun create(artista: Artista): Artista = dbQuery {
        ArtistasTable.insert {
            it[id] = artista.id
            it[name] = artista.name
            it[genre] = artista.genre
            it[createdAt] = artista.createdAt
            it[updatedAt] = artista.updatedAt
        }
        artista
    }

    override suspend fun findById(id: UUID): Artista? = dbQuery {
        ArtistasTable.select { ArtistasTable.id eq id }
            .map { it.toArtista() }
            .singleOrNull()
    }

    override suspend fun findAll(): List<Artista> = dbQuery {
        ArtistasTable.selectAll()
            .map { it.toArtista() }
    }

    override suspend fun update(id: UUID, updates: Map<String, Any?>): Artista? = dbQuery {
        val exists = ArtistasTable.select { ArtistasTable.id eq id }.count() > 0
        if (!exists) return@dbQuery null

        ArtistasTable.update({ ArtistasTable.id eq id }) {
            updates["name"]?.let { value -> it[name] = value as String }
            updates["genre"]?.let { value -> it[genre] = value as String? }
            updates["updatedAt"]?.let { value -> it[updatedAt] = value as java.time.Instant }
        }

        findById(id)
    }

    override suspend fun delete(id: UUID): Boolean = dbQuery {
        ArtistasTable.deleteWhere { ArtistasTable.id eq id } > 0
    }

    override suspend fun findWithRelations(id: UUID): Pair<Artista, List<Pair<Album, List<Track>>>>? = dbQuery {
        val artistaRow = ArtistasTable.select { ArtistasTable.id eq id }.singleOrNull()
            ?: return@dbQuery null
        val artista = artistaRow.toArtista()

        val albumes = (ArtistasTable innerJoin AlbumesTable)
            .select { ArtistasTable.id eq id }
            .map { it.toAlbum() }

        val albumesWithTracks = albumes.map { album ->
            val tracks = TracksTable
                .select { TracksTable.albumId eq album.id }
                .map { it.toTrack() }
            album to tracks
        }

        artista to albumesWithTracks
    }

    private fun ResultRow.toArtista() = Artista(
        id = this[ArtistasTable.id],
        name = this[ArtistasTable.name],
        genre = this[ArtistasTable.genre],
        createdAt = this[ArtistasTable.createdAt],
        updatedAt = this[ArtistasTable.updatedAt]
    )

    private fun ResultRow.toAlbum() = Album(
        id = this[AlbumesTable.id],
        title = this[AlbumesTable.title],
        releaseYear = this[AlbumesTable.releaseYear],
        artistId = this[AlbumesTable.artistId],
        createdAt = this[AlbumesTable.createdAt],
        updatedAt = this[AlbumesTable.updatedAt]
    )

    private fun ResultRow.toTrack() = Track(
        id = this[TracksTable.id],
        title = this[TracksTable.title],
        duration = this[TracksTable.duration],
        albumId = this[TracksTable.albumId],
        createdAt = this[TracksTable.createdAt],
        updatedAt = this[TracksTable.updatedAt]
    )
}


