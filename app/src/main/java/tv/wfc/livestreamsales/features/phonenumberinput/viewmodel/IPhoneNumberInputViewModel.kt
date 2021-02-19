package tv.wfc.livestreamsales.features.phonenumberinput.viewmodel

import androidx.lifecycle.LiveData
import tv.wfc.contentloader.viewmodel.IToBePreparedViewModel

interface IPhoneNumberInputViewModel: IToBePreparedViewModel {
    val phoneNumber: LiveData<String>
    val newCodeRequestWaitingTime: LiveData<Long>
    val isCodeRequestAvailable: LiveData<Boolean>
    val isConfirmationCodeSent: LiveData<Boolean>
    val isPhoneNumberCorrect: LiveData<Boolean>
    val canUserRequestCode: LiveData<Boolean>

    fun updatePhoneNumber(phoneNumber: String)
    fun requestConfirmationCode()
}