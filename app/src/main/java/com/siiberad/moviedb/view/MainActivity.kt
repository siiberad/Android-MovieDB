package com.siiberad.moviedb.view

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.siiberad.moviedb.R
import com.siiberad.moviedb.model.Genres
import com.siiberad.moviedb.model.Results
import com.siiberad.moviedb.view.adapter.MovieAdapter
import com.siiberad.moviedb.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var mvm: MainViewModel
    var idSelected = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        mvm = ViewModelProvider(this).get(MainViewModel::class.java)
        mvm.getDataGenre()
        observeViewModel()
        initListener()
    }

    private fun observeViewModel() {
        mvm.genres.observe(this, { genres ->
            initSpinner(genres)
        })

        mvm.movies.observe(this, { movies ->
            val movieAdapter = MovieAdapter(movies)
            movieAdapter.mOnItemClickListener = object : MovieAdapter.OnItemClickListener {
                override fun onClick(data: Results?) {
                    DetailMovieActivity.show(this@MainActivity, data?.id.toString())
                }
            }
            rv_movie.apply {
                layoutManager = LinearLayoutManager(context)
                movieAdapter.notifyDataSetChanged()
                adapter = movieAdapter
            }
        })
    }

    private fun initSpinner(list: List<Genres>) {
        spinner.item = list.map { it.name }
        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                mvm.getDataMoviewByGenre(list[position].id)
                idSelected = list[position].id!!
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
    }


    private fun initListener() {
        rv_movie.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                val countItem = linearLayoutManager.itemCount
                val lastVisiblePosition =
                    linearLayoutManager.findLastCompletelyVisibleItemPosition()
                val isLastPosition = countItem.minus(1) == lastVisiblePosition
                if (isLastPosition && mvm.page < mvm.totalPage) {
                    mvm.page = mvm.page.plus(1)
                    mvm.getDataMoviewByGenre(idSelected)
                }
            }
        })
    }

}