package com.ladecentro.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ladecentro.model.request.Role
import com.ladecentro.model.request.SignupRequest
import com.ladecentro.repository.AuthRepository
import com.ladecentro.service.auth.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val authRepository: AuthRepository) : ViewModel() {

    lateinit var authService: AuthService

    val loadingLD: LiveData<Boolean>
        get() = authRepository.loadingLD

    val email = MutableLiveData<String>()
    val name = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    fun signUp() {
        authRepository.service = authService
        val request =
            SignupRequest(email.value!!, name.value!!, password.value!!, setOf(Role("USER")))
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.signup(request)
        }
    }
}