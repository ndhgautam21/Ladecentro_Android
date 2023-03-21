package com.ladecentro.network

import com.ladecentro.model.CreateAddressRequest
import com.ladecentro.model.AddressResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.Objects

interface AddressApi {

    /**
     *
     */
    @GET(value = "/address/get")
    suspend fun getAllAddresses(@Header("x-auth-token") token: String
    ): Response<MutableList<AddressResponse>>

    /**
     *
     */
    @GET(value = "/address/get/{id}")
    suspend fun getAddress(
        @Header("x-auth-token") token: String,
        @Path("id") id: String
    ): Response<CreateAddressRequest>

    /**
     *
     */
    @POST(value = "/address/create")
    suspend fun createAddress(
        @Header("x-auth-token") token: String,
        @Body address: CreateAddressRequest
    ): Response<AddressResponse>

    /**
     *
     */
    @PUT(value = "/address/update/{id}")
    suspend fun updateAddress(
        @Header("x-auth-token") token: String,
        @Body address: CreateAddressRequest,
        @Path("id") id: String
    ): Response<Objects>

    /**
     *
     */
    @DELETE(value = "/address/delete/{id}")
    suspend fun deleteAddress(
        @Header("x-auth-token") token: String,
        @Path("id") id: String
    ): Response<Objects>
}