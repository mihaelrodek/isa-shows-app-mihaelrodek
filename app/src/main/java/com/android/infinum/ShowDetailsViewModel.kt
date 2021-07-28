package com.android.infinum

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

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