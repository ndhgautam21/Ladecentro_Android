package com.ladecentro.service.auth

import com.ladecentro.model.response.ErrorResponse

interface AuthService {
    fun success(token: String)
    fun error(error: ErrorResponse)
}