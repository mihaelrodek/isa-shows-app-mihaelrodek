package com.android.infinum.models

import androidx.annotation.DrawableRes

data class ReviewModel(
    val review: String,
    val rating: Float,
    @DrawableRes val imageResourceId: Int
)