package com.example.livestreamsales.model.application.authorization

data class PhoneNumberVerificationResult(
    val isPhoneNumberConfirmed: Boolean,
    val errorMessage: String? = null,
    val token: String? = null
)