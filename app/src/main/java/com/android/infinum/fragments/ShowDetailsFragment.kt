package com.android.infinum.fragments

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.infinum.*
import com.android.infinum.adapters.ReviewAdapter
import com.android.infinum.databinding.DialogAddReviewBinding
import com.android.infinum.databinding.FragmentShowDetailsBinding
import com.android.infinum.models.ReviewModel
import com.android.infinum.models.ShowsModel
import com.android.infinum.utils.DividerItemDecorator
import com.android.infinum.utils.ShowData
import com.google.android.material.bottomsheet.BottomSheetDialog

class ShowDetailsFragment : Fragment() {

    companion object {
        private const val EXTRA_SHOW = "EXTRA_SHOW"
        private const val SHARED_PREFS = "sharedPrefs"
        private const val USERNAME = "username"

    }

    private var user: String = ""

    private var _binding: FragmentShowDetailsBinding? = null

    private val binding get() = _binding!!

    private var reviewAdapter: ReviewAdapter? = null

    val args: com.android.infinum.ShowDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShowDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val showID = args.showID
        val showModel = ShowData.shows[showID - 1]

        val sharedPreferences =
            this.requireActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        sharedPreferences.edit()
        user = sharedPreferences.getString(getString(R.string.username), USERNAME).toString()

        binding.toolbar.title = showModel.name
        binding.ShowDetailsDescription.text = showModel.description
        binding.ShowDetailsImage.setImageResource(showModel.imageResourceId)

        binding.writeReview.setOnClickListener {
            showAddReviewBottomSheet(showModel)
        }

        setVisibles(showModel.reviews.isNullOrEmpty())

        if (reviewAdapter?.itemCount != 0) {
            setAverageRatingAndQuantity(showModel)
        }

        initShowsRecycler(showModel)

        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun setVisibles(bool: Boolean) {
        binding.emptyStateLabel.isVisible = bool
        binding.showRecyclerView.isVisible = bool.not()
        binding.ratingBarAverageText.isVisible = bool.not()
        binding.ratingBarAverage.isVisible = bool.not()
    }

    private fun initShowsRecycler(showModel: ShowsModel) {

        binding.showRecyclerView.layoutManager = LinearLayoutManager(context)
        val dividerItemDecoration: RecyclerView.ItemDecoration =
            DividerItemDecorator(context?.let {
                ContextCompat.getDrawable(
                    it,
                    R.drawable.divider
                )
            }!!)
        binding.showRecyclerView.addItemDecoration(dividerItemDecoration)

        binding.showRecyclerView.adapter = ReviewAdapter(showModel.reviews, user)
        reviewAdapter?.setReviews(showModel.reviews)
    }

    private fun showAddReviewBottomSheet(showModel: ShowsModel) {
        val dialog = context?.let { BottomSheetDialog(it) }

        val bottomSheetBinding = DialogAddReviewBinding.inflate(layoutInflater)
        dialog?.setContentView(bottomSheetBinding.root)

        bottomSheetBinding.submitButtonReview.setOnClickListener {

            if (bottomSheetBinding.ratingBar.rating.equals(0.0f)) {
                Toast.makeText(context, getString(R.string.rate_show), Toast.LENGTH_SHORT).show()
            } else {
                val reviewModel = ReviewModel(
                    bottomSheetBinding.reviewCommentEditor.text.toString(),
                    bottomSheetBinding.ratingBar.rating, R.drawable.super_mario
                )

                showModel.addReview(reviewModel)
                reviewAdapter?.setReviews(showModel.reviews)

                setAverageRatingAndQuantity(showModel)

                dialog?.dismiss()

                initShowsRecycler(showModel)

                if (reviewAdapter?.itemCount != 0) {
                    setVisibles(false)
                }

            }
        }
        bottomSheetBinding.cancelButton.setOnClickListener {
            dialog?.dismiss()
        }

        dialog?.show()
    }

    private fun setAverageRatingAndQuantity(showsModel: ShowsModel) {
        val helper = showsModel.reviews.map { it.rating }.average()
        binding.ratingBarAverageText.text =
            "${showsModel.reviews.size} REVIEWS, ${(Math.round(helper) * 100.0) / 100.0} AVERAGE"
        binding.ratingBarAverage.rating = helper.toFloat()
    }


}
