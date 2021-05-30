package com.vku.marshallelecom

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object AppConfig {
    const val BASE_URL = "http://192.168.1.2/cherrybridal/"
    const val IMAGE_URL = BASE_URL+"storage/app/"
    private val builder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
    val retrofit = builder.build()
}