package com.ladecentro.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ladecentro.model.Product
import com.ladecentro.network.ProductApi
import kotlin.Exception

class ProductPagingSource(
    private val token: String,
    private val productApi: ProductApi
) : PagingSource<Int, Product>() {

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        return try {
            val position = params.key ?: 1
            val response = productApi.getProducts(position, token)

            if (response.isSuccessful) {
                return LoadResult.Page(
                    data = response.body()?.content!!,
                    prevKey = if (position == 1) null else position - 1,
                    nextKey = if (position == response.body()?.totalPages) null else position + 1
                )
            } else return LoadResult.Error(Exception("something went wrong"))
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}