package com.example.moviesmanager.domain.model

import java.util.*

data class Movie(
    val name: String,
    val releasedData: Date,
    val studio: String,
    val duration: Int,
    val genre: Genre,
);
