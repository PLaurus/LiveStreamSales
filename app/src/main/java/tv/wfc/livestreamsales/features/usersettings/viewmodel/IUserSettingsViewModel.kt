package tv.wfc.livestreamsales.features.usersettings.viewmodel

import androidx.lifecycle.LiveData
import tv.wfc.contentloader.viewmodel.IToBePreparedViewModel

interface IUserSettingsViewModel: IToBePreparedViewModel{
    val name: LiveData<String>
    val surname: LiveData<String>
    val phoneNumber: LiveData<String>
    val email: LiveData<String>

    val cardNumber: LiveData<String>
//    val cardOwnerName: LiveData<String>
//    val validThrough: LiveData<DateTime>
//    val securityCode: LiveData<String>

    val isProcessingData: LiveData<Boolean>

    fun updateName(name: String)
    fun updateSurname(surname: String)
    fun updateEmail(email: String)
    fun saveUserPersonalData()

    fun updateCardNumber(number: CharSequence)
}