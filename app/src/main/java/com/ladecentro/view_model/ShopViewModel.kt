package com.ladecentro.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ladecentro.listener.UIState
import com.ladecentro.model.Product
import com.ladecentro.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShopViewModel @Inject constructor(
    productRepository: ProductRepository
) : ViewModel() {

    val uiState : Flow<PagingData<Product>> = productRepository.getProducts().cachedIn(viewModelScope)
}