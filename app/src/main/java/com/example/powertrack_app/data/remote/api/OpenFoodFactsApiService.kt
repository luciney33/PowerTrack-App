package com.example.powertrack_app.data.remote.api

import com.example.powertrack_app.data.remote.entity.OpenFoodFactsSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenFoodFactsApiService {
    @GET("cgi/search.pl")
    suspend fun searchFood(
        @Query("search_terms") searchTerms: String,
        @Query("search_simple") searchSimple: Int = 1,
        @Query("action") action: String = "process",
        @Query("json") json: Int = 1,
        @Query("fields") fields: String = "product_name,image_url"
    ): Response<OpenFoodFactsSearchResponse>
}