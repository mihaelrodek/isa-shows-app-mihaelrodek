package com.android.infinum.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.infinum.databinding.ItemReviewBinding
import com.android.infinum.models.ReviewModel
import com.android.infinum.models.responses.ReviewResponse

class ReviewAdapter(
    private var items: List<ReviewResponse>,
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
        holder.bind(items.get(position))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setReviews(reviews: List<ReviewResponse>) {
        items = reviews
        notifyDataSetChanged()
    }


    inner class ReviewViewHolder(private val binding: ItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ReviewResponse) {
            binding.userName.text = user
            binding.review.text = item.comment
            binding.starRating.text = item.rating.toString()
        }
    }


}