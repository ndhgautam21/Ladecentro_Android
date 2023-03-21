package com.ladecentro.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.ladecentro.network.AddressApi
import com.ladecentro.model.CreateAddressRequest
import com.ladecentro.model.AddressResponse
import com.ladecentro.model.ErrorResponse
import com.ladecentro.service.auth.AddressService
import com.ladecentro.util.Constants
import com.ladecentro.util.MyPreference
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

    private var addressList = MutableLiveData<MutableList<AddressResponse>>(mutableListOf())
    val addressesLD: LiveData<MutableList<AddressResponse>>
        get() = addressList

    private val addressMLD = MutableLiveData<CreateAddressRequest>()
    val addressLD: LiveData<CreateAddressRequest>
        get() = addressMLD

    /**
     * create address
     *
     * @param addressRequest address request
     * @return null
     */
    suspend fun createAddress(addressRequest: CreateAddressRequest) {
        loadingMLD.postValue(true)
        try {
            val response = request.createAddress(token, addressRequest)
            if (response.isSuccessful) {
                addressService.success(SAVE_MESSAGE)
            } else {
                val errorResponse: ErrorResponse =
                    Gson().fromJson(response.errorBody()?.charStream(), ErrorResponse::class.java)
                addressService.error(errorResponse)
            }
        } catch (e: Exception) {
            Log.e(ERROR_TAG, e.message!!)
            addressService.error(ErrorResponse(500, e.message!!, Date().toString()))
        } finally {
            loadingMLD.postValue(false)
        }
    }

    /**
     *
     */
    suspend fun getAllAddresses() {
        loadingMLD.postValue(true)
        try {
            val response = request.getAllAddresses(token)
            if (response.isSuccessful) {
                addressList.postValue(response.body())
            } else {
                val errorResponse: ErrorResponse =
                    Gson().fromJson(response.errorBody()?.charStream(), ErrorResponse::class.java)
                addressService.error(errorResponse)
            }
        } catch (e: Exception) {
            Log.e(ERROR_TAG, e.message!!)
            addressService.error(ErrorResponse(500, e.message!!, Date().toString()))
        } finally {
            loadingMLD.postValue(false)
        }
    }

    /**
     *
     */
    suspend fun getAddress(id: String) {
        loadingMLD.postValue(true)
        try {
            val response = request.getAddress(token, id)
            if (response.isSuccessful) {
                addressMLD.postValue(response.body())
            } else {
                val errorResponse: ErrorResponse =
                    Gson().fromJson(response.errorBody()?.charStream(), ErrorResponse::class.java)
                addressService.error(errorResponse)
            }
        } catch (e: Exception) {
            Log.e(ERROR_TAG, e.message!!)
            addressService.error(ErrorResponse(500, e.message!!, Date().toString()))
        } finally {
            loadingMLD.postValue(false)
        }
    }

    /**
     *
     */
    suspend fun updateAddress(addressRequest: CreateAddressRequest, id: String) {
        loadingMLD.postValue(true)
        try {
            val response = request.updateAddress(token, addressRequest, id)
            if (response.isSuccessful) {
                addressService.success(UPDATE_MESSAGE)
            } else {
                val errorResponse: ErrorResponse =
                    Gson().fromJson(response.errorBody()?.charStream(), ErrorResponse::class.java)
                addressService.error(errorResponse)
            }
        } catch (e: Exception) {
            Log.e(ERROR_TAG, e.message!!)
            addressService.error(ErrorResponse(500, e.message!!, Date().toString()))
        } finally {
            loadingMLD.postValue(false)
        }
    }

    /**
     *
     */
    suspend fun deleteAddress(id: String) {
        loadingMLD.postValue(true)
        try {
            val response = request.deleteAddress(token, id)
            if (response.isSuccessful) {
                addressService.success(DELETE_MESSAGE)
            } else {
                val errorResponse: ErrorResponse =
                    Gson().fromJson(response.errorBody()?.charStream(), ErrorResponse::class.java)
                addressService.error(errorResponse)
            }
        } catch (e: Exception) {
            Log.e(ERROR_TAG, e.message!!)
            addressService.error(ErrorResponse(500, e.message!!, Date().toString()))
        } finally {
            loadingMLD.postValue(false)
        }
    }

    companion object {
        const val ERROR_TAG = "ERROR_TAG"
        const val SAVE_MESSAGE = "Saved Successfully !!"
        const val UPDATE_MESSAGE = "Updated Successfully !!"
        const val DELETE_MESSAGE = "Deleted Successfully !!"
    }
}