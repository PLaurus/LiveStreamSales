package com.example.livestreamsales.model.application.phonenumberconfirmation

sealed class PhoneNumberConfirmationResult{
    data class PhoneNumberIsConfirmed(val token: String): PhoneNumberConfirmationResult()
    data class PhoneNumberIsNotConfirmed(val errorMessage: String? = null): PhoneNumberConfirmationResult()
}