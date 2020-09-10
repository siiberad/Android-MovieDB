package com.siiberad.moviedb.model

data class MovieReviewModel(
    val id: Int? = null,
    val page: Int? = null,
    val total_pages: Int? = null,
    val results: List<ResultsItemReview>? = null,
    val total_results: Int? = null
)

data class ResultsItemReview(
    val author: String? = null,
    val id: String? = null,
    val content: String? = null,
    val url: String? = null
)

