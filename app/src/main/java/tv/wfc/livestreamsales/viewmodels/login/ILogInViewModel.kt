package tv.wfc.livestreamsales.viewmodels.login

import androidx.lifecycle.LiveData
import tv.wfc.livestreamsales.model.network.rest.error.ResponseError

interface ILogInViewModel {
    val responseError: LiveData<ResponseError>
    val phoneNumber: LiveData<String>

    fun updatePhoneNumber(phoneNumber: String)
}