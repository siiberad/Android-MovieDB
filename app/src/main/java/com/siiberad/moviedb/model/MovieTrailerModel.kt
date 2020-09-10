package com.siiberad.moviedb.model

data class MovieTrailerModel(
    val id: Int? = null,
    val results: List<ResultsItemTrailer>? = null
)

data class ResultsItemTrailer(
    val site: String? = null,
    val size: Int? = null,
    val iso_3166_1: String? = null,
    val name: String? = null,
    val id: String? = null,
    val type: String? = null,
    val iso_639_1: String? = null,
    val key: String
)

