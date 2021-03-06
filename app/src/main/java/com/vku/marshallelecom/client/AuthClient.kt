package com.vku.marshallelecom.client

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

interface AuthClient {
    @POST(value = "api/login")
    fun login(@Query("username") username: String, @Query("password") password: String) : Call<JsonObject>

    @Headers("Connection: close")
    @POST(value = "api/socialite-login")
    fun socialiteLogin(@Query("provider") provider: String, @Query("access_token") access_token: String) : Call<JsonObject>

    @POST(value = "api/register")
    fun register(
        @Query("username") username: String,
        @Query("email") email: String,
        @Query("password") password : String,
        @Query("password_confirmation") confirm : String
    ) : Call<JsonObject>
    @POST("api/check-token")
    fun checkToken(@HeaderMap headers: Map<String, String>) : Call<JsonObject>

    @GET(value = "api/logout")
    fun logout(@HeaderMap headers : Map<String, String>) : Call<JsonObject>
}
