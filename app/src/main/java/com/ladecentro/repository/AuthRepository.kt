package com.ladecentro.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.ladecentro.network.AuthApi
import com.ladecentro.model.LoginRequest
import com.ladecentro.model.SignupRequest
import com.ladecentro.model.ErrorResponse
import com.ladecentro.service.auth.AuthService
import java.util.*
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authApi: AuthApi
) {
    lateinit var service: AuthService

    private val loadingMLD = MutableLiveData(false)
    val loadingLD: LiveData<Boolean>
        get() = loadingMLD

    /**
     * signup function
     */
    suspend fun signup(request: SignupRequest) {
        loadingMLD.postValue(true)
        try {
            val response = authApi.signup(request)
            if (response.isSuccessful) {
                service.success(response.body()!!)
            } else {
                val errorResponse: ErrorResponse =
                    Gson().fromJson(response.errorBody()?.charStream(), ErrorResponse::class.java)
                service.error(errorResponse)
            }
        } catch (e: Exception) {
            Log.e("error", e.message!!)
            service.error(ErrorResponse(500, e.message!!, Date().toString()))
        } finally {
            loadingMLD.postValue(false)
        }
    }

    /**
     * Login function
     */
    suspend fun login(request: LoginRequest) {
        loadingMLD.postValue(true)
        try {
            val response = authApi.login(request)
            if (response.isSuccessful) {
                service.success(response.body()!!)
            } else {
                val errorResponse: ErrorResponse =
                    Gson().fromJson(response.errorBody()?.charStream(), ErrorResponse::class.java)
                service.error(errorResponse)
            }
        } catch (e: Exception) {
            Log.e("error", e.message!!)
            service.error(ErrorResponse(500, e.message!!, Date().toString()))
        } finally {
            loadingMLD.postValue(false)
        }
    }
}