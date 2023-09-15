package com.bits.buymed.model

data class SignUpRequest(
    val username: String,
    val password: String,
    val password2: String
)
