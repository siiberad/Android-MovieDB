package com.siiberad.moviedb.services

import com.siiberad.moviedb.model.*
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {
    @GET(URL.GET_LIST_GENRES)
    fun getGenres(@Query("api_key") api_key: String): Single<MovieGenres>

    @GET(URL.GET_MOVIES_BY_GENRE)
    fun getMoviesByGenre(
        @Query("api_key") api_key: String,
        @Query("with_genres") with_genres: String,
        @Query("page") page: Int
    ): Observable<MovieByGenres>

    @GET(URL.GET_MOVIE_DETAIL)
    fun getMovieDetail(
        @Path("movie_id") movie_id: String,
        @Query("api_key") api_key: String
    ): Single<MovieDetailModel>

    @GET(URL.GET_MOVIE_TRAILER)
    fun getMovieTrailer(
        @Path("movie_id") movie_id: String,
        @Query("api_key") api_key: String
    ): Single<MovieTrailerModel>

    @GET(URL.GET_MOVIE_REVIEW)
    fun getMovieReview(
        @Path("movie_id") movie_id: String,
        @Query("api_key") api_key: String,
        @Query("page") page: Int
    ): Observable<MovieReviewModel>
}