package com.ladecentro.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ladecentro.model.CreateAddressRequest
import com.ladecentro.repository.AddressRepository
import com.ladecentro.service.auth.AddressService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddAddressViewModel @Inject constructor(
    private val repository: AddressRepository,
) : ViewModel() {

    val loadingLD: LiveData<Boolean>
        get() = repository.loadingLD

    val addressLD: LiveData<CreateAddressRequest>
        get() = repository.addressLD

    lateinit var addressId: String
    lateinit var addressService: AddressService

    fun getAddress() {
        repository.addressService = addressService
        viewModelScope.launch(Dispatchers.IO) {
            if (addressId != "") {
                repository.getAddress(addressId)
                isCreate.postValue(UPDATE_ADDRESS)
            }
        }
    }

    val name = MutableLiveData<String>()
    val fullAddress = MutableLiveData<String>()
    val pinCode = MutableLiveData<String>()
    val city = MutableLiveData<String>()
    val state = MutableLiveData<String>()
    val country = MutableLiveData<String>()
    val phoneNo = MutableLiveData<String>()
    val isCreate = MutableLiveData(SAVE_ADDRESS)

    fun saveAddress() {
        val address = CreateAddressRequest(
            id = null,
            name = name.value!!,
            address = fullAddress.value!!,
            pin_code = pinCode.value!!,
            city = city.value!!,
            state = state.value!!,
            country = country.value!!,
            phone_no = phoneNo.value!!
        )
        viewModelScope.launch(Dispatchers.IO) {
            if (addressId == "")
                repository.createAddress(address)
            else repository.updateAddress(address, addressId)
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