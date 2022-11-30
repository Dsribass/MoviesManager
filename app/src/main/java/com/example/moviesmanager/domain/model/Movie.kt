package com.example.moviesmanager.domain.model

import java.util.*

data class Movie(
    val id: UUID,
    val name: String,
    val releasedDate: Date,
    val studio: String,
    val duration: Int,
    val genre: Genre,
    val watched: Boolean = false,
    val rate: Int?,
);
