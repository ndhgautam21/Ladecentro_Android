package com.ladecentro.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ladecentro.listener.NetworkCallback
import com.ladecentro.model.User
import com.ladecentro.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val repository: ProfileRepository,
) : ViewModel() {


    val loadingLD: LiveData<Boolean>
        get() = repository.loadingLD

    val userLD: LiveData<User>
        get() = repository.userLD

    fun getUserDetails(callback: NetworkCallback) {

        viewModelScope.launch {
            repository.getUserDetails(callback)
        }
    }
}