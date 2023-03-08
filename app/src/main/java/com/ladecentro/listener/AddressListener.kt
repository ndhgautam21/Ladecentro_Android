package com.ladecentro.listener

import com.ladecentro.model.response.AddressResponse

interface AddressListener {

    fun deleteAddress(address: AddressResponse)

    fun getAddress(address: AddressResponse)
}