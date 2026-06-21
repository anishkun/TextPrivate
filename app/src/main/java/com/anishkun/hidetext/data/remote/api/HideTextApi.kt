package com.anishkun.hidetext.data.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

data class RegisterRequest(val phoneNumber: String, val publicKey: String)
data class KeyResponse(val publicKey: String?)

interface HideTextApi {
    @POST("/register")
    suspend fun registerPublicKey(@Body request: RegisterRequest): Response<Map<String, Boolean>>

    @GET("/key/{phoneNumber}")
    suspend fun getPublicKey(@Path("phoneNumber") phoneNumber: String): Response<KeyResponse>
}
