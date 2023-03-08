package com.ladecentro.model.response

/**
 * Error Response
 */
data class ErrorResponse(
    val status: Int,
    val message: String,
    val date_time: String
)

/**
 * get user response
 */
data class User(
    val id: String,
    val profile_image: ProfileImage?,
    val email: String,
    val name: String
) {
    data class ProfileImage(
        val data: String?
    )
}

/**
 * get address response
 */
data class AddressResponse(
    val id: String?,
    val name: String,
    val address: String,
    val phone_no: String
)