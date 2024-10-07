package com.example.ApiResponse

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    var success: Boolean = false,
    var message: String = "",
    var data: T? = null,
    var errors: List<String>? = null
)