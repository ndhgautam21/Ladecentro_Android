package com.ladecentro.service.auth

import com.ladecentro.model.ErrorResponse

interface AuthService {
    fun success(token: String)
    fun error(error: ErrorResponse)
}