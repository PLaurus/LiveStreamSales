package com.example.livestreamsales.viewmodels.telephonenumberinput

import androidx.lifecycle.LiveData

interface ITelephoneNumberInputViewModel {
    val phoneNumber: LiveData<String>
    val isVerificationCodeSent: LiveData<Boolean>
    val isTelephoneNumberCorrect: LiveData<Boolean>

    fun updatePhoneNumber(phoneNumber: String)
    fun requestVerificationCode()
}