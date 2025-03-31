package com.synagoguemanagement.synagoguemanagement.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import com.synagoguemanagement.synagoguemanagement.data.ShabbatTimesResponse

interface HebcalApiService {
    @GET("shabbat")
    fun getShabbatTimes(
            @Query("cfg") cfg: String = "json",
            @Query("geonameid") geonameid: Int,
            @Query("m") m: Int = 50
    ): Call<ShabbatTimesResponse>
}
