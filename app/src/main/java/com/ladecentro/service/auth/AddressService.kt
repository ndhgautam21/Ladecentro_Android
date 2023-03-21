package com.ladecentro.service.auth

import com.ladecentro.model.ErrorResponse

interface AddressService {
    fun success(message: String)
    fun error(error: ErrorResponse)
}