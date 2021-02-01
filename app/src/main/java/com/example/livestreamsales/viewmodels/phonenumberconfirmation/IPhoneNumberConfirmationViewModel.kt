package com.example.livestreamsales.viewmodels.phonenumberconfirmation

import androidx.lifecycle.LiveData
import com.example.livestreamsales.model.application.phonenumberconfirmation.PhoneNumberConfirmationResult
import com.example.livestreamsales.viewmodels.base.IToBePreparedViewModel

interface IPhoneNumberConfirmationViewModel: IToBePreparedViewModel {
    val phoneNumber: LiveData<String>
    val code: LiveData<String>
    val codeLength: LiveData<Int>
    val isCodeBeingChecked: LiveData<Boolean>
    val phoneNumberConfirmationResult: LiveData<PhoneNumberConfirmationResult>
    val phoneNumberConfirmationErrors: LiveData<String>
    val newCodeRequestWaitingTime: LiveData<Long>
    val isCodeRequestAvailable: LiveData<Boolean>
    val termsOfTheOfferUrl: LiveData<String>

    fun updatePhoneNumber(phoneNumber: String)
    fun updateCode(code: String)
    fun confirmPhoneNumber()
    fun requestNewCode()
}