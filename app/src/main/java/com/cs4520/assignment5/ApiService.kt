package com.cs4520.assignment5

import com.cs4520.assignment4.Api
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

// API GET request to get the products from the ENDPOINT url provided
// page is an optional query that can specify a certain page to load
// API call will return a Response of a list of products
interface ApiService {
    @GET(Api.ENDPOINT)
    suspend fun getProducts(@Query("page") page: Int? = null): Response<List<Product>>
}