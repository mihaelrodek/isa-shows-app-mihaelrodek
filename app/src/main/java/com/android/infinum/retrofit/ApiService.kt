package com.android.infinum.retrofit

import com.android.infinum.models.User
import com.android.infinum.models.requests.LoginRequest
import com.android.infinum.models.requests.RegisterRequest
import com.android.infinum.models.requests.ReviewRequest
import com.android.infinum.models.responses.LoginResponse
import com.android.infinum.models.responses.RegisterResponse
import com.android.infinum.models.responses.ReviewResponse
import com.android.infinum.models.responses.ShowResponse
import retrofit2.*
import retrofit2.http.*

interface ApiService {

    @POST("/users")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>

    @POST("/users/sign_in")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @PUT("/users")
    fun updateUser()

    @GET("/users/me")
    fun getCurrentUser(): Call<User>

    @POST("/reviews")
    fun addReview(@Body request : ReviewRequest): Call<ReviewResponse>

    //OPTIONAL
    @DELETE("/reviews/{id}")
    fun deleteReview(@Path("id") id: String) : Call<Void>

    //OPTIONAL
    @PATCH("/reviews/{id}")
    fun updateReview(@Path("id") id: String) : Call<ReviewResponse>

    @GET("/shows/{show_id}/reviews")
    fun getReviews(@Path("show_id") showId: Int): Call<List<ReviewResponse>>

    @GET("/shows")
    fun getShows(): Call<List<ShowResponse>>

    @GET("/shows/{id}")
    fun displayShow(@Path("id") id:String): Call<ShowResponse>

    //OPTIONAL
    @GET("/shows/top_rated")
    fun getTopRatedShows() : Call<List<ShowResponse>>

}