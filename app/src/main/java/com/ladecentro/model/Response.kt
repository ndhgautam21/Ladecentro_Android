package com.ladecentro.model

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

/**
 * product
 */
data class ProductPageable(
    val content: List<Product>,
    val last: Boolean,
    val totalPages: Int,
    val totalElements: Int,
    val size: Int,
    val number: Int,
    val first: Boolean,
    val numberOfElements: Int,
    val empty: Boolean
)

data class Product(
    val _id: String?,
    val product_name: String,
    val product_image: String,
    val selling_price: String
)