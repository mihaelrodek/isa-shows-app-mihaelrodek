package com.android.infinum.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.infinum.models.ReviewModel
import com.android.infinum.models.ShowsModel
import com.android.infinum.models.requests.ReviewRequest
import com.android.infinum.models.responses.ReviewResponse
import com.android.infinum.models.responses.ShowResponse
import com.android.infinum.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowDetailsViewModel : ViewModel() {

    private val reviewsLiveData: MutableLiveData<List<ReviewResponse>> by lazy {
        MutableLiveData<List<ReviewResponse>>()
    }

    private val addReviewLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    private val showLiveData: MutableLiveData<ShowResponse> by lazy {
        MutableLiveData<ShowResponse>()
    }

    fun getReviewsLiveData(): LiveData<List<ReviewResponse>> {
        return reviewsLiveData
    }

    fun getShowLiveData(): LiveData<ShowResponse> {
        return showLiveData
    }

    fun getReviews(showId: Int) {
        RetrofitClient.retrofit.getReviews(showId).enqueue(object : Callback<List<ReviewResponse>> {

            override fun onResponse(
                call: Call<List<ReviewResponse>>,
                response: Response<List<ReviewResponse>>
            ) {
                reviewsLiveData.value = response.body()
            }

            override fun onFailure(call: Call<List<ReviewResponse>>, t: Throwable) {
                reviewsLiveData.value = null
            }
        })
    }


    fun addReview(showId: Int, comment: String, rating : Int) {
        RetrofitClient.retrofit.addReview(ReviewRequest(rating,comment,showId)).enqueue(object : Callback<ReviewResponse>{
            override fun onResponse(
                call: Call<ReviewResponse>,
                response: Response<ReviewResponse>
            ) {
                addReviewLiveData.value = response.isSuccessful
            }

            override fun onFailure(call: Call<ReviewResponse>, t: Throwable) {
                addReviewLiveData.value = false
            }
        })
    }

    fun countReviews(showsModel: ShowsModel): Int {
        return showsModel.reviews.count()
    }

    fun getAverage(showsModel: ShowsModel): Float {
        return showsModel.reviews.map { it.rating }.average().toFloat()
    }
}