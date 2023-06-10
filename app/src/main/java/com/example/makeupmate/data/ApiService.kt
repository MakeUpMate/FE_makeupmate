package com.example.makeupmate.data

import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("predict")
    fun postImage(
        @Header("token") token: String,
        @Body body: Image64
    ): Call<PredictResponse>

}