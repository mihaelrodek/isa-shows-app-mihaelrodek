package com.android.infinum

import androidx.annotation.DrawableRes

data class ShowsModel(
    val id: String,
    val name: String,
    val description: String,
    var reviews: List<ReviewModel>,
    @DrawableRes val imageResourceId: Int
){
    fun addReview(reviewModel: ReviewModel){
        reviews = reviews + reviewModel
    }
}