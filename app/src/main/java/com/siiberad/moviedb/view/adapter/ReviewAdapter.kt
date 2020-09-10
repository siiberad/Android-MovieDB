package com.siiberad.moviedb.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.siiberad.moviedb.R
import com.siiberad.moviedb.model.ResultsItemReview
import kotlinx.android.synthetic.main.list_item_review.view.*

class ReviewAdapter(private val data: List<ResultsItemReview>) :
    RecyclerView.Adapter<ReviewAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_review, parent, false)
        )
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(data[position])
    }

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(data: ResultsItemReview) {
            itemView.tv_author.text = data.author
            itemView.tv_overview.text = data.content
        }
    }
}