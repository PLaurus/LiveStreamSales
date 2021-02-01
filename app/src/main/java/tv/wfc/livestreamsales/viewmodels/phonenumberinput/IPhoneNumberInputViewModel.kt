package tv.wfc.livestreamsales.viewmodels.phonenumberinput

import androidx.lifecycle.LiveData
import tv.wfc.livestreamsales.viewmodels.base.IToBePreparedViewModel

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