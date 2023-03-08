package com.ladecentro.service.auth

import com.ladecentro.model.response.ErrorResponse

interface AddressService {
    fun success(message: String)
    fun error(error: ErrorResponse)
}