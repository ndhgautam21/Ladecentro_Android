package com.ladecentro.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.ladecentro.listener.UIState
import com.ladecentro.model.CreateAddressRequest
import com.ladecentro.model.ErrorResponse
import com.ladecentro.network.AddressApi
import com.ladecentro.service.auth.AddressService
import com.ladecentro.util.Constants
import com.ladecentro.util.MyPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.util.*
import javax.inject.Inject

class AddressRepository @Inject constructor(
    private val request: AddressApi,
    private val myPreference: MyPreference
) {
    lateinit var addressService: AddressService

    private val token: String
        get() = myPreference.getStoredTag(Constants.PreferenceToken.name)

    private val loadingMLD = MutableLiveData(false)
    val loadingLD: LiveData<Boolean>
        get() = loadingMLD

    private val addressMLD = MutableLiveData<CreateAddressRequest>()
    val addressLD: LiveData<CreateAddressRequest>
        get() = addressMLD

    /**
     * create address
     *
     * @param addressRequest address request
     * @return null
     */
    fun createAddress(addressRequest: CreateAddressRequest) = flow {
        emit(UIState.Loading)
        val response = request.createAddress(token, addressRequest)
        if (response.isSuccessful) {
            emit(UIState.Success(data = response.body()!!))
            return@flow
        }
        val errorResponse: ErrorResponse = Gson()
            .fromJson(response.errorBody()?.charStream(), ErrorResponse::class.java)
        emit(UIState.Error(errorResponse.message))
    }.catch { emit(UIState.Error(it.message!!)) }

    /**
     *
     */
    fun getAllAddresses() = flow {
        emit(UIState.Loading)
        val response = request.getAllAddresses(token)
        if (response.isSuccessful) {
            emit(UIState.Success(data = response.body()!!))
            return@flow
        }
        val errorResponse: ErrorResponse = Gson()
            .fromJson(response.errorBody()?.charStream(), ErrorResponse::class.java)
        emit(UIState.Error(errorResponse.message))
    }.catch { emit(UIState.Error(it.message!!)) }

    /**
     *
     */
    fun getAddress(id: String) = flow {
        emit(UIState.Loading)
        val response = request.getAddress(token, id)
        if (response.isSuccessful) {
            emit(UIState.Success(data = response.body()!!))
            return@flow
        }
        val errorResponse: ErrorResponse = Gson()
            .fromJson(response.errorBody()?.charStream(), ErrorResponse::class.java)
        emit(UIState.Error(errorResponse.message))
    }.catch { emit(UIState.Error(it.message!!)) }

    /**
     *
     */
    fun updateAddress(addressRequest: CreateAddressRequest, id: String) = flow {
        emit(UIState.Loading)
        val response = request.updateAddress(token, addressRequest, id)
        if (response.isSuccessful) {
            emit(UIState.Success(data = response.body()!!))
            return@flow
        }
        val errorResponse: ErrorResponse = Gson()
            .fromJson(response.errorBody()?.charStream(), ErrorResponse::class.java)
        emit(UIState.Error(errorResponse.message))
    }.catch { emit(UIState.Error(it.message!!)) }

    /**
     *
     */
    fun deleteAddress(id: String) = flow {
        emit(UIState.Loading)
        val response = request.deleteAddress(token, id)
        //request.getAllAddresses(token)
        if (response.isSuccessful) {
            emit(UIState.Success(data = response.body()!!))
            return@flow
        }
        val errorResponse: ErrorResponse = Gson()
            .fromJson(response.errorBody()?.charStream(), ErrorResponse::class.java)
        emit(UIState.Error(errorResponse.message))
    }.catch { emit(UIState.Error(it.message!!)) }

    companion object {
        const val ERROR_TAG = "ERROR_TAG"
        const val SAVE_MESSAGE = "Saved Successfully !!"
        const val UPDATE_MESSAGE = "Updated Successfully !!"
    }
}