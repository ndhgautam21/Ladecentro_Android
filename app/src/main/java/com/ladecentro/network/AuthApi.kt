package com.ladecentro.network

import com.ladecentro.model.LoginRequest
import com.ladecentro.model.SignupRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST(value = "/auth/signup")
    suspend fun signup(@Body request: SignupRequest) : Response<String>

    @POST(value = "/auth/login")
    suspend fun login(@Body request : LoginRequest) : Response<String>
}