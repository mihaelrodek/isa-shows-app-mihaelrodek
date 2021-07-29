package com.android.infinum.fragments

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.LocusId
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.infinum.*
import com.android.infinum.adapters.ReviewAdapter
import com.android.infinum.databinding.DialogAddReviewBinding
import com.android.infinum.databinding.FragmentShowDetailsBinding
import com.android.infinum.models.ReviewModel
import com.android.infinum.models.ShowsModel
import com.android.infinum.models.responses.ReviewResponse
import com.android.infinum.utils.DividerItemDecorator
import com.android.infinum.utils.ShowData
import com.android.infinum.viewmodels.ShowDetailsViewModel
import com.android.infinum.viewmodels.ShowsViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class ShowDetailsFragment : Fragment() {

    companion object {
        private const val SHARED_PREFS = "sharedPrefs"
        private const val USERNAME = "username"
        private const val AT_SEPARATOR = "@"

    }

    private var user: String = ""

    private var _binding: FragmentShowDetailsBinding? = null

    private val binding get() = _binding!!

    private var reviewAdapter: ReviewAdapter? = null

    val args: ShowDetailsFragmentArgs by navArgs()

    private val viewModel: ShowDetailsViewModel by viewModels()

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

        viewModel.getShowLiveData().observe(this.viewLifecycleOwner){ show ->
            if(show != null){
                binding.ratingBarAverageText.text = "${show.noOfReviews} Reviews, ${show.averageRating} Average"
                binding.ratingBarAverage.rating = show.averageRating.toFloat()
                binding.toolbar.title = show.title
                binding.ShowDetailsDescription.text = show.description
                binding.ShowDetailsImage.setImageResource(R.drawable.daredevil)
            }else
            Toast.makeText(context, "Fetching tv shows failed", Toast. LENGTH_SHORT). show()
        }

        viewModel.getReviewsLiveData().observe(this.viewLifecycleOwner) { review ->
            if (!review.isNullOrEmpty()) {
                setVisibles(false)
                initShowsRecycler(review)
            } else if(review.isEmpty()){
                setVisibles(false)
            }else
                Toast.makeText(context, "Fetching reviews failed", Toast.LENGTH_SHORT).show()
        }

        viewModel.getAddReviewsLiveData().observe(this.viewLifecycleOwner){ success->
            if(success){
                viewModel.getShow(args.showID.toString())
                viewModel.getReviews(args.showID)
            }else Toast.makeText(context, "Adding review failed", Toast.LENGTH_SHORT).show()
        }

        viewModel.getShow(args.showID.toString())
        viewModel.getReviews(args.showID)

        binding.writeReview.setOnClickListener {
            showAddReviewBottomSheet(showID)
        }

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

    private fun initShowsRecycler(reviewResponses: List<ReviewResponse>) {

        binding.showRecyclerView.layoutManager = LinearLayoutManager(context)
        val dividerItemDecoration: RecyclerView.ItemDecoration =
            DividerItemDecorator(context?.let {
                ContextCompat.getDrawable(
                    it,
                    R.drawable.divider
                )
            }!!)
        binding.showRecyclerView.addItemDecoration(dividerItemDecoration)
        binding.showRecyclerView.adapter = ReviewAdapter(reviewResponses, user.substringBefore(
            AT_SEPARATOR))
    }

    private fun showAddReviewBottomSheet(showId: Int) {
        val dialog = context?.let { BottomSheetDialog(it) }

        val bottomSheetBinding = DialogAddReviewBinding.inflate(layoutInflater)
        dialog?.setContentView(bottomSheetBinding.root)

        bottomSheetBinding.submitButtonReview.setOnClickListener {

            if (bottomSheetBinding.ratingBar.rating.equals(0.0f)) {
                Toast.makeText(context, getString(R.string.rate_show), Toast.LENGTH_SHORT).show()
            } else {

                viewModel.addReview(showId,bottomSheetBinding.reviewCommentEditor.text.toString(),bottomSheetBinding.ratingBar.rating.toInt())
                //viewModel.getShow(showId.toString())

                dialog?.dismiss()
            }
        }
        bottomSheetBinding.cancelButton.setOnClickListener {
            dialog?.dismiss()
        }
        dialog?.show()
    }
}
