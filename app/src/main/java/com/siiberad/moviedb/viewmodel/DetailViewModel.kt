package com.siiberad.moviedb.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.siiberad.moviedb.model.MovieDetailModel
import com.siiberad.moviedb.model.MovieReviewModel
import com.siiberad.moviedb.model.MovieTrailerModel
import com.siiberad.moviedb.model.ResultsItemReview
import com.siiberad.moviedb.services.MovieService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch

class DetailViewModel(application: Application) : BaseViewModel(application) {

    private val movieApiService = MovieService()
    private val disposable = CompositeDisposable()

    var page = 1
    var totalPage = 0

    fun getMovieData(id: String) {
        fetchDetail(id)
        fetchReview(id)
    }

    val detail = MutableLiveData<MovieDetailModel>()
    val trailer = MutableLiveData<String>()
    val review = MutableLiveData<List<ResultsItemReview>>()

    private fun fetchDetail(id: String) {
        disposable.add(
            movieApiService.getMovieDetail(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<MovieDetailModel>() {
                    override fun onSuccess(t: MovieDetailModel) {
                        detailRetrieved(t)
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }
                })
        )
    }

    private fun detailRetrieved(g: MovieDetailModel) {
        launch {
            detail.postValue(g)
        }
    }


    fun getTrailer(id: String) = fetchTrailer(id)
    private fun fetchTrailer(id: String) {
        disposable.add(
            movieApiService.getMovieTrailer(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<MovieTrailerModel>() {
                    override fun onSuccess(t: MovieTrailerModel) {
                        t.results?.get(0)?.key?.let { trailerRetrieved(it) }
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }
                })
        )
    }

    private fun trailerRetrieved(g: String) {
        launch {
            trailer.postValue(g)
        }
    }

    fun getReview(id: String) = fetchReview(id)
    private fun fetchReview(id: String) {
        disposable.add(
            movieApiService.getMovieReview(id, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<MovieReviewModel>() {
                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }
                    override fun onNext(t: MovieReviewModel) {
                        reviewRetrieved(t.results!!)
                        totalPage = t.total_pages!!
                    }
                    override fun onComplete() {}
                })
        )
    }

    private fun reviewRetrieved(g: List<ResultsItemReview>) {
        launch {
            review.postValue(g)
        }
    }

}