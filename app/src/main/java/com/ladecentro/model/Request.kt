package com.ladecentro.model

/**
 * Signup Request
 */
data class SignupRequest(
    val email: String,
    val name: String,
    val password: String,
    val roles: Set<String>
)

/**
 * Login Request
 */
data class LoginRequest(
    val email: String,
    val password: String
)

/**
 * Update profile request.
 */
data class ProfileUpdateRequest(
    val name: String
)

/**
 * create Address request.
 */
data class CreateAddressRequest(
    val id: String?,
    val name: String,
    val address: String,
    val pin_code: String,
    val city: String,
    val state: String,
    val country: String,
    val phone_no: String,
)
