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

        fun buildIntent(activity: Activity, showModel: String): Intent {
            val intent = Intent(activity, ShowDetailsActivity::class.java)
            intent.putExtra(EXTRA_SHOW, showModel)
            return intent
        }
    }

    private var user: String = ""

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

        val showModel = shows[showID - 1]

        binding.ShowDetailsTitle.text = showModel.name
        binding.ShowDetailsDescription.text = showModel.description
        binding.ShowDetailsImage.setImageResource(showModel.imageResourceId)

        binding.writeReview.setOnClickListener {
            showAddReviewBottomSheet(showModel)
        }

        if(showModel.reviews.isEmpty()){
            setVisibles(true)
        }
        if (reviewAdapter?.itemCount != 0) {
            setVisibles(false)
            setAverageRatingAndQuantity(showModel)
        }
        
        initShowsRecycler(showModel)

        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
        
    }
    
    private fun setVisibles(boolean: Boolean){
        binding.emptyStateLabel.isVisible = boolean
        binding.showRecyclerView.isVisible = !boolean
        binding.ratingBarAverage.isVisible = !boolean
        binding.ratingBarAverageText.isVisible = !boolean
    }

    private fun initShowsRecycler(showModel: ShowsModel) {

        binding.showRecyclerView.layoutManager = LinearLayoutManager(this)

        binding.showRecyclerView.adapter = ReviewAdapter(showModel.reviews, user)
        reviewAdapter?.setReviews(showModel.reviews)
    }

    private fun showAddReviewBottomSheet(showModel: ShowsModel) {
        val dialog = BottomSheetDialog(this)

        val bottomSheetBinding = DialogAddReviewBinding.inflate(layoutInflater)
        dialog.setContentView(bottomSheetBinding.root)

        bottomSheetBinding.submitButtonReview.setOnClickListener {

            if (bottomSheetBinding.ratingBar.rating.equals(0.0f)) {
                Toast.makeText(this, "Please rate the show.", Toast.LENGTH_SHORT).show()
            } else {
                val reviewModel = ReviewModel(
                    bottomSheetBinding.reviewComment.text.toString(),
                    bottomSheetBinding.ratingBar.rating, R.drawable.super_mario
                )

                showModel.addReview(reviewModel)
                reviewAdapter?.setReviews(showModel.reviews)

                setAverageRatingAndQuantity(showModel)

                dialog.dismiss()

                initShowsRecycler(showModel)

                if (reviewAdapter?.itemCount != 0) {
                    setVisibles(false)
                }

            }
        }

        dialog.show()
    }

    private fun setAverageRatingAndQuantity(showsModel: ShowsModel) {

        val helper = showsModel.reviews.map { it.rating }.toList()
        binding.ratingBarAverageText.text =
            "${helper.size} REVIEWS, ${(Math.round(helper.average()) * 100.0) / 100.0} AVERAGE"
        binding.ratingBarAverage.rating = (helper.average()).toFloat()
    }


}
