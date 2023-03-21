package com.ladecentro.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.ladecentro.network.ProfileApi
import com.ladecentro.listener.NetworkCallback
import com.ladecentro.model.ProfileUpdateRequest
import com.ladecentro.model.ErrorResponse
import com.ladecentro.model.User
import com.ladecentro.util.Constants
import com.ladecentro.util.MyPreference
import okhttp3.MultipartBody
import java.util.*
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val request: ProfileApi,
    private val preference: MyPreference,
) {
    private val token: String
        get() = preference.getStoredTag(Constants.PreferenceToken.name)

    private val loadingMLD = MutableLiveData(false)
    val loadingLD: LiveData<Boolean>
        get() = loadingMLD

    private val userMLD = MutableLiveData<User>()
    val userLD: LiveData<User>
        get() = userMLD

    /**
     * get user details
     *
     * @return void
     */
    suspend fun getUserDetails(callback: NetworkCallback) {
        loadingMLD.postValue(true)
        try {
            val response = request.getProfileDetail(token)
            if (response.isSuccessful) {
                userMLD.postValue(response.body())
            } else {
                val errorResponse: ErrorResponse =
                    Gson().fromJson(response.errorBody()?.charStream(), ErrorResponse::class.java)
                callback.onError(errorResponse)
            }
        } catch (e: Exception) {
            Log.e("error", e.message!!)
            callback.onError(ErrorResponse(500, "something went wrong !!", Date().toString()))
        }
        loadingMLD.postValue(false)
    }

    /**
     * update profile details
     *
     * @param profileRequest
     * @return void
     */
    suspend fun updateProfile(callback: NetworkCallback, profileRequest: ProfileUpdateRequest) {
        loadingMLD.postValue(true)
        try {
            val response = request.updateProfile(token, profileRequest)
            if (response.isSuccessful) {
                callback.onSuccess("Updated successfully")
            } else {
                val errorResponse: ErrorResponse =
                    Gson().fromJson(response.errorBody()?.charStream(), ErrorResponse::class.java)
                callback.onError(errorResponse)
            }
        } catch (e: Exception) {
            Log.e("error", e.message!!)
            callback.onError(ErrorResponse(500, e.message!!, Date().toString()))
        } finally {
            loadingMLD.postValue(false)
        }
    }

    /**
     * update profile picture
     *
     * @param file : Multipart.Part
     * @return
     */
    suspend fun updateProfileImage(callback: NetworkCallback, file: MultipartBody.Part?) {
        loadingMLD.postValue(true)
        try {
            val response = request.updateProfileImage(token, file)
            if (response.isSuccessful) {
                callback.onSuccess("Updated successfully")
            } else {
                val errorResponse: ErrorResponse =
                    Gson().fromJson(response.errorBody()?.charStream(), ErrorResponse::class.java)
                callback.onError(errorResponse)
            }
        } catch (e: Exception) {
            Log.e("error", e.message!!)
            callback.onError(ErrorResponse(500, e.message!!, Date().toString()))
        } finally {
            loadingMLD.postValue(false)
        }
    }
}