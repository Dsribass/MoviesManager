package com.example.moviesmanager.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.moviesmanager.model.Genre
import com.example.moviesmanager.model.Movie
import java.time.LocalDate
import java.util.*

@Entity(tableName = MovieEntity.TABLE_NAME)
data class MovieEntity(
    @PrimaryKey
    @ColumnInfo(name = ID)
    val id: String,
    @ColumnInfo(name = NAME)
    val name: String,
    @ColumnInfo(name = RELEASE_DATE)
    val releaseDate: String,
    @ColumnInfo(name = STUDIO)
    val studio: String,
    @ColumnInfo(name = DURATION)
    val duration: Int,
    @ColumnInfo(name = GENRE)
    val genre: String,
    @ColumnInfo(name = RATE)
    val rate: Int?,
) {
    companion object {
        const val TABLE_NAME = "movies"
        const val ID = "id"
        const val NAME = "name"
        const val RELEASE_DATE = "release_date"
        const val STUDIO = "studio"
        const val DURATION = "duration"
        const val GENRE = "genre"
        const val RATE = "rate"

        fun fromModel(movie: Movie): MovieEntity {
            return MovieEntity(
                id = movie.id.toString(),
                name = movie.name,
                releaseDate = movie.releaseDate.toString(),
                studio = movie.studio,
                duration = movie.duration,
                genre = movie.genre.toString(),
                rate = movie.rate,
            )
        }
    }

    fun toModel(): Movie {
        return Movie(
            id = UUID.fromString(id),
            name = name,
            releaseDate = LocalDate.parse(releaseDate),
            studio = studio,
            duration = duration,
            genre = Genre.valueOf(genre),
            rate = rate,
        )
    }
}