package tv.wfc.livestreamsales.features.login.viewmodel

import androidx.lifecycle.LiveData
import tv.wfc.livestreamsales.features.rest.model.ResponseError

interface ILogInViewModel {
    val responseError: LiveData<ResponseError>
    val phoneNumber: LiveData<String>

    fun updatePhoneNumber(phoneNumber: String)
}