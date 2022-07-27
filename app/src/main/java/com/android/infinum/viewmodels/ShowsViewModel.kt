package com.android.infinum.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.infinum.models.responses.ShowResponse
import com.android.infinum.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowsViewModel : ViewModel(){

    private val showsLiveData: MutableLiveData<List<ShowResponse>> by lazy {
        MutableLiveData<List<ShowResponse>>()
    }

    fun getShowsLiveData(): LiveData<List<ShowResponse>> {
        return showsLiveData
    }

    fun getShows() {
        RetrofitClient.retrofit.getShows().enqueue(object : Callback<List<ShowResponse>> {
            override fun onResponse(
                call: Call<List<ShowResponse>>,
                response: Response<List<ShowResponse>>
            ) {
                showsLiveData.value=response.body()
            }

            override fun onFailure(call: Call<List<ShowResponse>>, t: Throwable) {
                showsLiveData.value = null
            }


        })
    }

}