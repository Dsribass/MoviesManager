package com.example.moviesmanager.model

import java.time.LocalDate
import java.util.*

data class Movie(
    val id: UUID,
    val name: String,
    val releaseDate: LocalDate,
    val studio: String,
    val duration: Int,
    val genre: Genre,
    val rate: Int?,
) {
    val wasWatched get() = rate != null
}
