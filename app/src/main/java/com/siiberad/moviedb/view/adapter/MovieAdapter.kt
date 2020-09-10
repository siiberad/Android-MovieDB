package com.siiberad.moviedb.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.siiberad.moviedb.BuildConfig
import com.siiberad.moviedb.R
import com.siiberad.moviedb.model.Results
import kotlinx.android.synthetic.main.list_item_film.view.*

class MovieAdapter(private var data: ArrayList<Results>) : RecyclerView.Adapter<MovieAdapter.Holder>() {
    var mOnItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onClick(data: Results?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_film, parent, false)
        )
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener {
            mOnItemClickListener?.onClick(data[position])
        }
    }

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        var rating = 0.0
        fun bind(data: Results) {
            rating = data.vote_average!!
            itemView.tvTitle.text = data.title
            itemView.tvReleaseDate.text = data.release_date
            itemView.tvDesc.text = data.overview
            itemView.ratingBar.numStars = 5
            itemView.ratingBar.stepSize = 0.5.toFloat()
            itemView.ratingBar.rating = (rating / 2).toFloat()
            Glide.with(itemView.context)
                .load(BuildConfig.IMAGE_URL + data.poster_path)
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.ic_baseline_image_24)
                        .transform(RoundedCorners(18))
                )
                .into(itemView.imgPhoto)
        }
    }

    fun refreshAdapter(resultTheMovieDb: List<Results>) {
        this.data.addAll(resultTheMovieDb)
        notifyItemRangeChanged(0, this.data.size)
    }
}