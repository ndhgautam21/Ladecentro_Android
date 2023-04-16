package com.ladecentro.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ladecentro.listener.UIState
import com.ladecentro.model.Category
import com.ladecentro.model.Product
import com.ladecentro.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ShopViewModel @Inject constructor(
    productRepository: ProductRepository
) : ViewModel() {

    val uiState : Flow<PagingData<Product>> = productRepository.getProducts().cachedIn(viewModelScope)

    val categoryUiState: Flow<UIState<List<Category>>> = productRepository.getCategories()
}