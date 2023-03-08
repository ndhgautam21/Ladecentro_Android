package com.ladecentro.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ladecentro.model.response.User
import com.ladecentro.repository.ProfileRepository
import com.ladecentro.service.auth.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val repository: ProfileRepository,
) : ViewModel() {

    lateinit var authService: AuthService

    val loadingLD: LiveData<Boolean>
        get() = repository.loadingLD

    val userLD: LiveData<User>
        get() = repository.userLD

    fun getUserDetails() {
        repository.service = authService
        viewModelScope.launch {
            repository.getUserDetails()
        }
    }
}