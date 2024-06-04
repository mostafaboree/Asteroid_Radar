package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.models.PictureOfDay
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query



private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


val retrofit: Retrofit by lazy {
    Retrofit.Builder()
        .baseUrl(Constants.BASE_URL).addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
}


interface NeoApiService {


    @GET("planetary/apod?")
    suspend fun getPictureOfDay(
        @Query("api_key") api_key: String = Constants.API_KEY
    ): PictureOfDay

    @GET("neo/rest/v1/feed?")
    suspend fun getAsteroidsData(
        @Query("api_key") api_key: String = Constants.API_KEY
    ): String


}

object NeoApi {
    val remote: NeoApiService by lazy {
        retrofit.create(NeoApiService::class.java)
    }
}