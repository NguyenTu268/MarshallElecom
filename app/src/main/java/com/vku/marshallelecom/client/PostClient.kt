package com.vku.marshallelecom.client

import com.google.gson.JsonObject
import com.vku.marshallelecom.model.PostAPI
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*


interface PostClient {

    @GET(value = "api/posts")
    fun getPosts(@HeaderMap headers : Map<String, String>): Call<PostAPI>

    @Multipart
    @POST("api/posts")
    fun createPost(@HeaderMap headers : Map<String, String>, @Part media: MultipartBody.Part, @Part("description") description: String): Call<JsonObject>

    @POST("api/posts/like")
    fun likePost(@HeaderMap headers: Map<String, String>, @Query("post_id") post_id: Int): Call<JsonObject>
}