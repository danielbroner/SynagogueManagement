package com.synagoguemanagement.synagoguemanagement.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.synagoguemanagement.synagoguemanagement.data.ShabbatItem
import com.synagoguemanagement.synagoguemanagement.data.ShabbatTimesResponse
import com.synagoguemanagement.synagoguemanagement.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShabbatViewModel : ViewModel() {
    private val _shabbatTimes = MutableLiveData<List<ShabbatItem>>()
    val shabbatTimes: LiveData<List<ShabbatItem>> = _shabbatTimes


    fun fetchShabbatTimes(geonameid: Int) {
        val call = RetrofitInstance.api.getShabbatTimes(geonameid = geonameid)

        call.enqueue(object : Callback<ShabbatTimesResponse> {
            override fun onResponse(
                call: Call<ShabbatTimesResponse>,
                response: Response<ShabbatTimesResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { data ->
                        val filteredTimes = data.items.filter {
                            it.category == "candles" || it.category == "havdalah"
                        }
                        _shabbatTimes.value = filteredTimes
                    }
                }
            }

            override fun onFailure(call: Call<ShabbatTimesResponse>, t: Throwable) {
                _shabbatTimes.value = emptyList()
            }
        })
    }
}
