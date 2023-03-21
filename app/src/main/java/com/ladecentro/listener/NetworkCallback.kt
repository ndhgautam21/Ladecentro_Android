package com.ladecentro.listener

import com.ladecentro.model.ErrorResponse

interface NetworkCallback {

    fun onSuccess(message: String)
    fun onError(error: ErrorResponse)
}