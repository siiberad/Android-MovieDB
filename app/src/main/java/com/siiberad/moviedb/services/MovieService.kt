package com.siiberad.moviedb.services

import com.siiberad.moviedb.BuildConfig
import com.siiberad.moviedb.model.*
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MovieService {
    private val api = Retrofit.Builder()
        .baseUrl(BuildConfig.BASEURL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(MovieApi::class.java)

    fun getGenres(): Single<MovieGenres> {
        return api.getGenres(BuildConfig.API_KEY)
    }

    fun getMoviesByGenres(idGenre: String, page: Int): Observable<MovieByGenres> {
        return api.getMoviesByGenre(BuildConfig.API_KEY, idGenre, page)
    }

    fun getMovieDetail(idMovie: String): Single<MovieDetailModel> {
        return api.getMovieDetail(idMovie, BuildConfig.API_KEY)
    }

    fun getMovieTrailer(idMovie: String): Single<MovieTrailerModel> {
        return api.getMovieTrailer(idMovie, BuildConfig.API_KEY)
    }

    fun getMovieReview(idMovie: String, page: Int): Observable<MovieReviewModel> {
        return api.getMovieReview(idMovie, BuildConfig.API_KEY, page)
    }
}