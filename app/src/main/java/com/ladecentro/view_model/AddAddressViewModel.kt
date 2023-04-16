package com.ladecentro.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ladecentro.listener.UIState
import com.ladecentro.listener.UIState.Loading
import com.ladecentro.model.AddressResponse
import com.ladecentro.model.CreateAddressRequest
import com.ladecentro.repository.AddressRepository
import com.ladecentro.service.auth.AddressService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.util.Objects
import javax.inject.Inject

@HiltViewModel
class AddAddressViewModel @Inject constructor(
    private val repository: AddressRepository,
) : ViewModel() {

    lateinit var addressId: String

    private val _address =
        MutableStateFlow<UIState<CreateAddressRequest>>(Loading)
    val addresses: StateFlow<UIState<CreateAddressRequest>>
        get() = _address

    private val _createAddress =
        MutableStateFlow<UIState<AddressResponse>>(Loading)
    val createAddress: StateFlow<UIState<AddressResponse>>
        get() = _createAddress


    fun getAddress(id: String): Flow<UIState<CreateAddressRequest>> {
        isCreate.postValue(UPDATE_ADDRESS)
        return repository.getAddress(id)
    }

    val name = MutableLiveData<String>()
    val fullAddress = MutableLiveData<String>()
    val pinCode = MutableLiveData<String>()
    val city = MutableLiveData<String>()
    val state = MutableLiveData<String>()
    val country = MutableLiveData<String>()
    val phoneNo = MutableLiveData<String>()
    val isCreate = MutableLiveData(SAVE_ADDRESS)

    private fun createAddressPayload() =
        CreateAddressRequest(
            id = null,
            name = name.value!!,
            address = fullAddress.value!!,
            pin_code = pinCode.value!!,
            city = city.value!!,
            state = state.value!!,
            country = country.value!!,
            phone_no = phoneNo.value!!
        )


    fun saveAddress() {
        val payload = createAddressPayload()
        viewModelScope.launch {
            if (addressId == "")
                repository.createAddress(payload).flowOn(Dispatchers.IO).collect { _createAddress.value = it}
            else repository.updateAddress(payload, addressId).flowOn(Dispatchers.IO).collect { _createAddress.value = it }
        }
    }

    fun setAddressValue(addressResponse: CreateAddressRequest?) {
        addressResponse?.let { add ->
            name.postValue(add.name)
            fullAddress.postValue(add.address)
            pinCode.postValue(add.pin_code)
            city.postValue(add.city)
            state.postValue(add.state)
            country.postValue(add.state)
            phoneNo.postValue(add.phone_no)
        }
    }

    companion object {
        const val SAVE_ADDRESS = "Save Address"
        const val UPDATE_ADDRESS = "Update Address"
    }
}