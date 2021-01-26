package com.example.livestreamsales.viewmodels.phoneconfirmation

import androidx.lifecycle.LiveData
import com.example.livestreamsales.model.application.viewmodel.ViewModelPreparationState

interface IPhoneConfirmationViewModel {
    val dataPreparationState: LiveData<ViewModelPreparationState>
    val phoneNumber: LiveData<String>
    val code: LiveData<Int>
    val codeLength: LiveData<Int>
    val isCodeBeingChecked: LiveData<Boolean>
    val phoneConfirmationResult: LiveData<PhoneConfirmationResult?>
    val phoneConfirmationErrors: LiveData<String?>
    val newCodeRequestWaitingTime: LiveData<Long>
    val isCodeRequestAvailable: LiveData<Boolean>
    val termsOfTheOfferUrl: LiveData<String>

    fun updatePhoneNumber(phoneNumber: String)
    fun updateCode(code: String)
    fun confirmPhoneNumber()
    fun requestNewCode()

    enum class PhoneConfirmationResult{
        PHONE_CONFIRMED_NEED_REGISTRATION,
        PHONE_CONFIRMED,
        PHONE_NOT_CONFIRMED
    }
}