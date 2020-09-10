package com.siiberad.moviedb.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.siiberad.moviedb.model.Genres
import com.siiberad.moviedb.model.MovieByGenres
import com.siiberad.moviedb.model.MovieGenres
import com.siiberad.moviedb.model.Results
import com.siiberad.moviedb.services.MovieService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

import kotlinx.coroutines.launch

class MainViewModel(application: Application) : BaseViewModel(application) {

    private val movieApiService = MovieService()
    private val disposable = CompositeDisposable()

    var page = 1
    var totalPage = 0

    val genres = MutableLiveData<List<Genres>>()
    val movies = MutableLiveData<List<Results>>()

    fun getDataGenre() = fetchGenre()

    private fun fetchGenre() {
        disposable.add(
            movieApiService.getGenres()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<MovieGenres>() {
                    override fun onSuccess(t: MovieGenres) {
                        genresRetrieved(t.genres!!)
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }
                })
        )
    }

    private fun genresRetrieved(g: List<Genres>) {
        launch {
            genres.postValue(g)
        }
    }

    fun getDataMoviewByGenre(id: Int?) = fetchMovieByGenre(id.toString())

    private fun fetchMovieByGenre(id: String?) {
        disposable.add(
            movieApiService.getMoviesByGenres(id!!, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<MovieByGenres>() {
                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                    override fun onNext(t: MovieByGenres) {
                        moviewByGenresRetrieved(t.results!!)
                        totalPage = t.total_pages!!
                    }

                    override fun onComplete() {}
                })
        )
    }

    private fun moviewByGenresRetrieved(g: List<Results>) {
        launch {
            movies.postValue(g)
        }
    }

}