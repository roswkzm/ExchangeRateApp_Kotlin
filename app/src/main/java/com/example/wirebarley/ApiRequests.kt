package com.example.wirebarley

import com.example.wirebarley.api.ApiJson
import retrofit2.Call
import retrofit2.http.GET

interface ApiRequests {
    @GET("/live?access_key=a5d92de29b861f55f97fcc105721ed22&currencies=KRW,JPY,PHP")
    fun getmoney(): Call<ApiJson>
}