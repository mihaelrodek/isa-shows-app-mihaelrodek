package com.android.infinum.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.infinum.models.ReviewModel

class ShowDetailsViewModel :ViewModel() {


    private val reviews = mutableListOf<ReviewModel>()

    private val reviewLiveData: MutableLiveData<List<ReviewModel>> by lazy {
        MutableLiveData<List<ReviewModel>>()
    }

    fun getReviewsLiveData(): MutableLiveData<List<ReviewModel>> {
        return reviewLiveData
    }

    fun initReviews() {
        reviewLiveData.value = reviews
    }

    fun addReview(review: ReviewModel) {
        reviews.add(review)
        reviewLiveData.value = reviews
    }
}