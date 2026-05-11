package com.example.powertrack_app.data.remote.api

import com.example.powertrack_app.data.remote.entity.ExerciseDbResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ExerciseDbApiService {
    @GET("exercises/name/{name}")
    suspend fun getExercisesByName(
        @Path("name", encoded = false) name: String
    ): Response<List<ExerciseDbResponse>>
}