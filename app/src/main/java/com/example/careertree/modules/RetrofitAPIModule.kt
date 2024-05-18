package com.example.careertree.modules

import com.example.careertree.utility.Constants.BASE_URL
import com.example.careertree.service.api.DataAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RetrofitAPIModule {

    @Singleton
    @Provides
    fun getRetrofit():Retrofit{

        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    @Singleton
    @Provides
    fun getDataAPI(retrofit: Retrofit): DataAPI {

        return retrofit.create(DataAPI::class.java)
    }
}