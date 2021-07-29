package com.android.infinum.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.infinum.models.requests.LoginRequest
import com.android.infinum.models.responses.LoginResponse
import com.android.infinum.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel(){

    private val loginLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun getLoginLiveData(): LiveData<Boolean> {
        return loginLiveData
    }

    fun login(email: String, password: String) {
        RetrofitClient.retrofit.login(LoginRequest(email, password))
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    loginLiveData.value = response.isSuccessful

                    //with (preferences.edit()){
                    //    putString(ACCESS_TOKEN, response.headers() [ACCESS_TOKEN])
                    //    putString(CLIENT, response. headers() [CLIENT])
                    //    putString (UID, response. headers () [UID])
                    //    apply()
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    loginLiveData.value = false
                }

            })
    }

}