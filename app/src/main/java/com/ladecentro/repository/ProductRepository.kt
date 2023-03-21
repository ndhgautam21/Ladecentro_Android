package com.ladecentro.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.ladecentro.network.ProductApi
import com.ladecentro.paging.ProductPagingSource
import com.ladecentro.util.Constants
import com.ladecentro.util.MyPreference
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productApi: ProductApi,
    private val myPreference: MyPreference
) {

    private val token: String
        get() = myPreference.getStoredTag(Constants.PreferenceToken.name)

    fun getProducts() = Pager(
        config = PagingConfig(pageSize = 5, maxSize = 100),
        pagingSourceFactory = { ProductPagingSource(token, productApi) },
        initialKey = 1
    ).flow
}