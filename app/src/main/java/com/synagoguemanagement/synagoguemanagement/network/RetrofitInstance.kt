package com.synagoguemanagement.synagoguemanagement.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.hebcal.com/")
            .addConverterFactory(GsonConverterFactory.create())
        .build()
    }

val api: HebcalApiService by lazy {
    retrofit.create(HebcalApiService::class.java)
}
}
