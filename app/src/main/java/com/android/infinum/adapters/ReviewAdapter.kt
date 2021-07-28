package com.android.infinum.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.infinum.databinding.ItemReviewBinding

class ReviewAdapter(
    private var items: List<_root_ide_package_.com.android.infinum.models.ReviewModel>,
    private var user: String
) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReviewViewHolder {

        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setReviews(reviews: List<_root_ide_package_.com.android.infinum.models.ReviewModel>) {
        items = reviews
        notifyDataSetChanged()
    }


    inner class ReviewViewHolder(private val binding: ItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: _root_ide_package_.com.android.infinum.models.ReviewModel) {
            binding.userName.text = user
            binding.review.text = item.review
            binding.starRating.text = item.rating.toString()
        }
    }


}