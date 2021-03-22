package tv.wfc.livestreamsales.features.phonenumberconfirmation.viewmodel

import androidx.lifecycle.LiveData
import tv.wfc.contentloader.viewmodel.INeedPreparationViewModel
import tv.wfc.livestreamsales.application.model.phonenumberconfirmation.PhoneNumberConfirmationResult

interface IPhoneNumberConfirmationViewModel: INeedPreparationViewModel {
    val phoneNumber: LiveData<String>
    val code: LiveData<String>
    val codeLength: LiveData<Int>
    val isCodeBeingChecked: LiveData<Boolean>
    val phoneNumberConfirmationResult: LiveData<PhoneNumberConfirmationResult>
    val phoneNumberConfirmationErrors: LiveData<String>
    val newCodeRequestWaitingTime: LiveData<Long>
    val isCodeRequestAvailable: LiveData<Boolean>
    val termsOfTheOfferUrl: LiveData<String>

    fun updateCode(code: String)
    fun confirmPhoneNumber()
    fun requestNewCode()
}