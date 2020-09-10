package com.siiberad.moviedb.model

data class MovieGenres(
    val genres: List<Genres>?
)

data class Genres(
    var id: Int?,
    var name: String?,
)