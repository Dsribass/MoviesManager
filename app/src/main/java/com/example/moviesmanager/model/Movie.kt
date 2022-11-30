package com.example.moviesmanager.model

import java.util.*

data class Movie(
    val id: UUID,
    val name: String,
    val releaseDate: Date,
    val studio: String,
    val duration: Int,
    val genre: Genre,
    val rate: Int?,
) {
    val wasWatched get() = rate != null
}
