package com.ladecentro.network

import com.ladecentro.model.ProductPageable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ProductApi {

    @GET("/product/get")
    suspend fun getProducts(
        @Query("page_number") pageNo: Int,
        @Header("x-auth-token") token: String
    ): Response<ProductPageable>
}
