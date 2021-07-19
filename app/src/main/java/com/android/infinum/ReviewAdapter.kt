package com.android.infinum

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.infinum.databinding.ItemReviewBinding

class ReviewAdapter(
    private var items: List<ReviewModel>,
    private var user: String
) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>()  {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReviewAdapter.ReviewViewHolder {

        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context))
        return ReviewViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ReviewAdapter.ReviewViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setReviews(reviews: List<ReviewModel>) {
        items = reviews
        notifyDataSetChanged()
    }


    inner class ReviewViewHolder(private val binding: ItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ReviewModel) {
            binding.userName.text = user
            binding.review.text = item.review
            binding.starRating.text = item.rating.toString()

        }
    }


}