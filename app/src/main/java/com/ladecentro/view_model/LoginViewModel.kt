package com.ladecentro.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ladecentro.model.request.LoginRequest
import com.ladecentro.repository.AuthRepository
import com.ladecentro.service.auth.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    val loading: LiveData<Boolean>
        get() = authRepository.loadingLD

    lateinit var authService: AuthService

    val errorEmail = MutableLiveData(false)
    val errorPassword = MutableLiveData(false)

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    fun login() {
        authRepository.service = authService
        if (errorEmail.value!! || errorPassword.value!!) {
            return
        }
        val request = LoginRequest(email.value!!, password.value!!)
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.login(request)
        }
    }
}