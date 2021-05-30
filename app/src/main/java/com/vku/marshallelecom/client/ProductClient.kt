package com.vku.marshallelecom.client

import com.vku.marshallelecom.model.Product
import com.vku.marshallelecom.model.ProductAPI
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface ProductClient {
    @GET(value = "api/products")
    fun getProducts(@QueryMap options: Map<String, String>): Call<ProductAPI>

    @GET(value = "api/products/{id}")
    fun getProduct(@Path("id") id : Int): Call<Product>
}