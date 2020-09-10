package com.siiberad.moviedb.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.siiberad.moviedb.BuildConfig
import com.siiberad.moviedb.R
import com.siiberad.moviedb.view.adapter.ReviewAdapter
import com.siiberad.moviedb.viewmodel.DetailViewModel
import kotlinx.android.synthetic.main.activity_detail_movie.*


class DetailMovieActivity : AppCompatActivity(), YouTubePlayerCallback {

    private lateinit var dvm: DetailViewModel
    private lateinit var yt: YouTubePlayer
    var id = ""

    companion object {
        fun show(source: Activity, id: String) {
            val intent = Intent(source, DetailMovieActivity::class.java).apply {
                putExtras(Bundle().apply {
                    putString("ID", id)
                })
            }
            source.startActivityForResult(intent, 12)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_movie)
        supportActionBar?.hide()
        id = intent?.extras?.getString("ID").toString()
        dvm = ViewModelProvider(this).get(DetailViewModel::class.java)
        dvm.getMovieData(id)
        observeViewModel()
        initView(id)
        initListener()
    }

    private fun initView(id: String) {
        btnTrailer.setOnClickListener {
            if (!expandable_layout.isExpanded) {
                expandable_layout.expand(true)
                dvm.getTrailer(id)
            } else {
                expandable_layout.collapse(true)
                yt.pause()
            }
        }
        nested_scroll?.viewTreeObserver?.addOnScrollChangedListener {
            if (nested_scroll.scrollY < 1000) {
                imgCover.visibility = VISIBLE
            } else {
                imgCover.visibility = INVISIBLE
            }
        }
    }

    private fun observeViewModel() {
        dvm.detail.observe(this, {
            val rating = it.vote_average!!
            tvName.text = it.title
            tvRating.text = "$rating/10"
            tvRelease.text = it.release_date
            tvPopularity.text = it.popularity.toString()
            tvOverview.text = it.overview
            tvName.isSelected = true
            ratingBar.numStars = 5
            ratingBar.stepSize = 0.5.toFloat()
            ratingBar.rating = (rating / 2).toFloat()

            Glide.with(this)
                .load(BuildConfig.IMAGE_URL + it.backdrop_path)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgCover)

            Glide.with(this)
                .load(BuildConfig.IMAGE_URL + it.poster_path)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgPhoto)

        })

        dvm.trailer.observe(this, {
            if (expandable_layout.isExpanded) {
                youtube_player_view.getYouTubePlayerWhenReady(this)
                kotlin.run { yt.loadVideo(it, 0f) }
                yt.play()
            }
        })

        dvm.review.observe(this, { review ->
            val reviewAdapter = ReviewAdapter(review)
            rv_review.apply {
                layoutManager = LinearLayoutManager(context)
                reviewAdapter.notifyDataSetChanged()
                adapter = reviewAdapter
            }
        })
    }

    override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
        yt = youTubePlayer
    }


    private fun initListener() {
        rv_review.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                val countItem = linearLayoutManager.itemCount
                val lastVisiblePosition =
                    linearLayoutManager.findLastCompletelyVisibleItemPosition()
                val isLastPosition = countItem.minus(1) == lastVisiblePosition
                if (isLastPosition && dvm.page < dvm.totalPage) {
                    dvm.page = dvm.page.plus(1)
                    dvm.getReview(id)
                }
            }
        })
    }
}