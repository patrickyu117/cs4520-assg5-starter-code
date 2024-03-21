package com.cs4520.assignment5

import com.cs4520.assignment4.Api
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Retrofit client that takes the JSON from the API call
object RetrofitClient {
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Api.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

// API client to get the JSON from the site
object ApiClient {
    val apiService: ApiService by lazy {
        RetrofitClient.retrofit.create(ApiService::class.java)
    }
}