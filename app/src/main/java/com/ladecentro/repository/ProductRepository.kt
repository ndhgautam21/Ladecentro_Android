package com.ladecentro.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.google.gson.Gson
import com.ladecentro.listener.UIState
import com.ladecentro.model.ErrorResponse
import com.ladecentro.network.ProductApi
import com.ladecentro.paging.ProductPagingSource
import com.ladecentro.util.Constants
import com.ladecentro.util.MyPreference
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productApi: ProductApi,
    private val myPreference: MyPreference
) {

    private val token: String
        get() = myPreference.getStoredTag(Constants.PreferenceToken.name)

    fun getProducts() = Pager(
        config = PagingConfig(pageSize = 4, maxSize = 100),
        pagingSourceFactory = { ProductPagingSource(token, productApi) },
        initialKey = 1
    ).flow

    fun getCategories() = flow {
        emit(UIState.Loading)
        val response = productApi.getCategories(token)
        if (response.isSuccessful) {
            emit(UIState.Success(data = response.body()))
            return@flow
        }
        val errorResponse: ErrorResponse = Gson()
            .fromJson(response.errorBody()?.charStream(), ErrorResponse::class.java)
        emit(UIState.Error(errorResponse.message))
    }.catch { emit(UIState.Error(it.message!!)) }
}