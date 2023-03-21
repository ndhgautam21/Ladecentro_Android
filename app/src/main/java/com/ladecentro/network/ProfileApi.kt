package com.ladecentro.network

import com.ladecentro.model.ProfileUpdateRequest
import com.ladecentro.model.User
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part
import java.util.*

interface ProfileApi {

    @GET(value = "/user/get")
    suspend fun getProfileDetail(@Header("x-auth-token") token: String): Response<User>

    @PUT(value = "/user/update")
    suspend fun updateProfile(
        @Header("x-auth-token") token: String,
        @Body request: ProfileUpdateRequest
    ): Response<Objects>

    @Multipart
    @PUT(value = "/user/profile-image")
    suspend fun updateProfileImage(
        @Header("x-auth-token") token: String,
        @Part file: MultipartBody.Part?,
        @Part("delete") delete: String = "deleted"
    ): Response<User>
}