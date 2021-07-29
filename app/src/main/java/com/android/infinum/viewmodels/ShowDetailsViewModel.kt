package com.android.infinum.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.infinum.models.ReviewModel
import com.android.infinum.models.ShowsModel

class ShowDetailsViewModel : ViewModel() {

    private val reviewsLiveData: MutableLiveData<List<ReviewModel>> by lazy {
        MutableLiveData<List<ReviewModel>>()
    }

    fun getReviewsLiveData(): LiveData<List<ReviewModel>> {
        return reviewsLiveData
    }

    fun initReviews(showsModel: ShowsModel) {
        reviewsLiveData.value = showsModel.reviews
    }

    fun addReview(showsModel: ShowsModel, review: ReviewModel) {
        showsModel.addReview(review)
        reviewsLiveData.value = showsModel.reviews
    }

    fun countReviews(showsModel: ShowsModel): Int {
        return showsModel.reviews.count()
    }

    fun getAverage(showsModel: ShowsModel): Float {
        return showsModel.reviews.map { it.rating }.average().toFloat()
    }
}