package com.ladecentro.api

import com.ladecentro.model.request.LoginRequest
import com.ladecentro.model.request.SignupRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthRequest {

    @POST(value = "/auth/signup")
    suspend fun signup(@Body request: SignupRequest) : Response<String>

    @POST(value = "/auth/login")
    suspend fun login(@Body request : LoginRequest) : Response<String>
}