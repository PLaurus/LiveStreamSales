package com.example.livestreamsales.viewmodels.phoneinput

import androidx.lifecycle.LiveData

interface IPhoneInputViewModel {
    val phoneNumber: LiveData<String>
    val newCodeRequestWaitingTime: LiveData<Long>
    val isCodeRequestAvailable: LiveData<Boolean>
    val isVerificationCodeSent: LiveData<Boolean?>
    val isTelephoneNumberCorrect: LiveData<Boolean>
    val canUserRequestCode: LiveData<Boolean>

    fun updatePhoneNumber(phoneNumber: String)
    fun requestVerificationCode()
}