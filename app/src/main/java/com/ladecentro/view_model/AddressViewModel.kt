package com.ladecentro.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ladecentro.model.AddressResponse
import com.ladecentro.repository.AddressRepository
import com.ladecentro.service.auth.AddressService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val repository: AddressRepository
) :
    ViewModel() {

    val loadingLD: LiveData<Boolean>
        get() = repository.loadingLD

    val addressesLD: LiveData<MutableList<AddressResponse>>
        get() = repository.addressesLD

    lateinit var addressService: AddressService

    fun getAddresses() {
        repository.addressService = addressService
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllAddresses()
        }
    }

    fun deleteAddress(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val job = viewModelScope.launch {
                repository.deleteAddress(id)
            }
            job.join()
            viewModelScope.launch {
                repository.getAllAddresses()
            }
        }
    }
}