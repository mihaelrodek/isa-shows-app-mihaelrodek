package com.android.infinum

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.infinum.ShowData.shows
import com.android.infinum.databinding.ActivityShowDetailsBinding
import com.android.infinum.databinding.DialogAddReviewBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class ShowDetailsActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_SHOW = "EXTRA_SHOW"
        private const val USERNAME = "username"
        private const val SHARED_PREFS = "sharedPrefs"

        fun buildIntent(activity: Activity, showId: String) : Intent {
            val intent = Intent(activity, ShowDetailsActivity::class.java)
            intent.putExtra(EXTRA_SHOW, showId)
            return intent
        }
    }
    private var user : String = ""

    private lateinit var binding: ActivityShowDetailsBinding

    private var reviewAdapter: ReviewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val showID = intent.extras?.getString(EXTRA_SHOW)?.toInt()!!

        val sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        sharedPreferences.edit()
        user = sharedPreferences.getString(getString(R.string.username), USERNAME).toString()

        binding = ActivityShowDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = showID-1

        binding.ShowDetailsTitle.text = shows[id].name
        binding.ShowDetailsDescription.text = shows[id].description
        binding.ShowDetailsImage.setImageResource(shows[id].imageResourceId)

        binding.writeReview.setOnClickListener{
            showAddReviewBottomSheet(id)
        }

        if(shows[id].reviews.isEmpty()){
            binding.emptyStateLabel.isVisible = true
            binding.showRecyclerView.isVisible = false
            binding.ratingBarAverage.isVisible = false
            binding.ratingBarAverageText.isVisible = false

        }else if(reviewAdapter?.itemCount != 0){
            binding.emptyStateLabel.isVisible = false
            binding.showRecyclerView.isVisible = true
            binding.ratingBarAverage.isVisible = true
            binding.ratingBarAverageText.isVisible = true
            setAverageRatingAndQuantity(shows[id])

        }
        initShowsRecycler(id)

        binding.toolbar.setNavigationOnClickListener { onBackPressed() }


    }

    private fun initShowsRecycler(id: Int){

        binding.showRecyclerView.layoutManager = LinearLayoutManager(this)

        binding.showRecyclerView.adapter = ReviewAdapter(shows[id].reviews, user)
        reviewAdapter?.setReviews(shows[id].reviews)
    }

    private fun showAddReviewBottomSheet(id: Int) {
        val dialog = BottomSheetDialog(this)

        val bottomSheetBinding = DialogAddReviewBinding.inflate(layoutInflater)
        dialog.setContentView(bottomSheetBinding.root)

        bottomSheetBinding.submitButtonReview.setOnClickListener {

            if(bottomSheetBinding.ratingBar.rating.compareTo(0.0)==0)
            {
                Toast.makeText(this, "Please rate the show.", Toast.LENGTH_SHORT).show()
            }else{
                val show = shows[id]

                val reviewModel = ReviewModel(bottomSheetBinding.reviewComment.text.toString(),
                    bottomSheetBinding.ratingBar.rating, R.drawable.super_mario)

                show.addReview(reviewModel)
                reviewAdapter?.setReviews(show.reviews)

                setAverageRatingAndQuantity(show)

                dialog.dismiss()

                initShowsRecycler(id)
                
                if(reviewAdapter?.itemCount != 0) {

                    binding.emptyStateLabel.isVisible = false
                    binding.showRecyclerView.isVisible = true
                    binding.ratingBarAverage.isVisible = true
                    binding.ratingBarAverageText.isVisible = true
                }

            }
        }

        dialog.show()
    }

    private fun setAverageRatingAndQuantity(showsModel: ShowsModel){

        val revs = showsModel.reviews
        var sum = 0.0
        for (rev in revs){
            sum+=rev.rating
        }

        binding.ratingBarAverageText.text = "${revs.size} REVIEWS, ${(Math.round(sum/revs.size)*100.0)/100.0} AVERAGE"
        binding.ratingBarAverage.rating = (sum/revs.size).toFloat()
    }


}
