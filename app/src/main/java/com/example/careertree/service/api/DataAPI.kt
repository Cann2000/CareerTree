package com.example.careertree.service.api

import com.example.careertree.model.infodata.InfoMainData
import com.example.careertree.model.infodata.InfoProfileTextData
import retrofit2.Response
import retrofit2.http.GET

interface DataAPI {

    @GET("Cann2000/projectApi/main/main_data.json")
    suspend fun getMainData():Response<InfoMainData>

    @GET("Cann2000/projectApi/main/profile_text_data.json")
    suspend fun getProfileTextData():Response<InfoProfileTextData>

}