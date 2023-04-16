package com.ladecentro.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ladecentro.listener.UIState
import com.ladecentro.model.AddressResponse
import com.ladecentro.repository.AddressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val repository: AddressRepository
) : ViewModel() {

    private val _addresses =
        MutableStateFlow<UIState<MutableList<AddressResponse>>>(UIState.Loading)
    val addresses: StateFlow<UIState<MutableList<AddressResponse>>>
        get() = _addresses

    init {
        getAddresses()
    }

    fun getAddresses() {
        viewModelScope.launch {
            repository.getAllAddresses().flowOn(Dispatchers.IO).collect { _addresses.value = it }
        }
    }

    fun deleteAddress(id: String): Flow<UIState<Objects>> {
        return repository.deleteAddress(id).flowOn(Dispatchers.IO)
    }
}