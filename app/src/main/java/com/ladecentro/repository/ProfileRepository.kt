package com.ladecentro.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.ladecentro.api.ProfileRequest
import com.ladecentro.model.request.ProfileUpdateRequest
import com.ladecentro.model.response.ErrorResponse
import com.ladecentro.model.response.User
import com.ladecentro.service.auth.AuthService
import com.ladecentro.util.Constants
import com.ladecentro.util.MyPreference
import okhttp3.MultipartBody
import java.util.*
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val request: ProfileRequest,
    private val preference: MyPreference,
) {
    lateinit var service: AuthService

    val token: String
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
    suspend fun getUserDetails() {
        loadingMLD.postValue(true)
        try {
            val response = request.getProfileDetail(token)
            if (response.isSuccessful) {
                userMLD.postValue(response.body())
            } else {
                val errorResponse: ErrorResponse =
                    Gson().fromJson(response.errorBody()?.charStream(), ErrorResponse::class.java)
                service.error(errorResponse)
            }
        } catch (e: Exception) {
            Log.e("error", e.message!!)
            service.error(ErrorResponse(500, "something went wrong !!", Date().toString()))
        }
        loadingMLD.postValue(false)
    }

    /**
     * update profile details
     *
     * @param profileRequest
     * @return void
     */
    suspend fun updateProfile(profileRequest: ProfileUpdateRequest) {
        loadingMLD.postValue(true)
        try {
            val response = request.updateProfile(token, profileRequest)
            if (response.isSuccessful) {
                service.success("Updated successfully")
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
     * update profile picture
     *
     * @param file : Multipart.Part
     * @return
     */
    suspend fun updateProfileImage(file: MultipartBody.Part?) {
        loadingMLD.postValue(true)
        try {
            val response = request.updateProfileImage(token, file)
            if (response.isSuccessful) {
                service.success("Updated successfully")
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