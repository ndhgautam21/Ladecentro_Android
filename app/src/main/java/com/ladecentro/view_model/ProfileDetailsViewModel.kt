package com.ladecentro.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ladecentro.listener.NetworkCallback
import com.ladecentro.model.ProfileUpdateRequest
import com.ladecentro.model.User
import com.ladecentro.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody.Part
import javax.inject.Inject

@HiltViewModel
class ProfileDetailsViewModel @Inject constructor(
    private val repository: ProfileRepository
) : ViewModel() {

    val loadingLD: LiveData<Boolean>
        get() = repository.loadingLD

    val userLd: LiveData<User>
        get() = repository.userLD

    val name = MutableLiveData<String>()
    val image = MutableLiveData<String?>()
    val errorEnable = MutableLiveData(false)

    fun getUserDetails(callback: NetworkCallback) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getUserDetails(callback)
        }
    }

    fun updateProfile(callback: NetworkCallback) {
        if (errorEnable.value!!) {
            return
        }
        val request = ProfileUpdateRequest(name = name.value!!)
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateProfile(callback, request)
        }
    }

    fun setUserValues(user: User) {
        name.postValue(user.name)
        image.postValue(user.profile_image?.data)
    }

    fun updateProfileImage(callback: NetworkCallback, part: Part?) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateProfileImage(callback, part)
        }
    }
}