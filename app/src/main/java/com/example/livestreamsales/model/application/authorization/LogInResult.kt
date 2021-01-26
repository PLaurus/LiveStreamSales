package com.example.livestreamsales.model.application.authorization

data class LogInResult(
    val isLoggedIn: Boolean,
    val needRegistration: Boolean,
    val errorMessage: String? = null
)