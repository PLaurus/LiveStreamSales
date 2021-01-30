package com.example.livestreamsales.viewmodels.phoneinput

import androidx.lifecycle.LiveData
import com.example.livestreamsales.viewmodels.base.IToBePreparedViewModel

interface IPhoneInputViewModel: IToBePreparedViewModel {
    val phoneNumber: LiveData<String>
    val newCodeRequestWaitingTime: LiveData<Long>
    val isCodeRequestAvailable: LiveData<Boolean>
    val isVerificationCodeSent: LiveData<Boolean>
    val isTelephoneNumberCorrect: LiveData<Boolean>
    val canUserRequestCode: LiveData<Boolean>

    fun updatePhoneNumber(phoneNumber: String)
    fun requestVerificationCode()
}