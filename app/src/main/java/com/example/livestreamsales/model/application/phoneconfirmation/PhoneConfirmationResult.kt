package com.example.livestreamsales.model.application.phoneconfirmation

sealed class PhoneConfirmationResult{
    data class PhoneIsConfirmed(val token: String): PhoneConfirmationResult()
    data class PhoneIsNotConfirmed(val errorMessage: String? = null): PhoneConfirmationResult()
}