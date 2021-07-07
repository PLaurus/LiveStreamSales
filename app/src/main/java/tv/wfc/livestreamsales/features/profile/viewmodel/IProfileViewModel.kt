package tv.wfc.livestreamsales.features.profile.viewmodel

import androidx.lifecycle.LiveData
import tv.wfc.contentloader.viewmodel.INeedPreparationViewModel

interface IProfileViewModel: INeedPreparationViewModel{
    val isUserAuthorized: LiveData<Boolean>

    val name: LiveData<String>
    val nameError: LiveData<NameError?>
    val surname: LiveData<String>
    val surnameError: LiveData<SurnameError?>
    val phoneNumber: LiveData<String>
    val email: LiveData<String>
    val emailError: LiveData<EmailError?>
    val isCurrentUserPersonalInformationCorrect: LiveData<Boolean>
    val isUserPersonalInformationSaved: LiveData<Unit>

    val isAnyOperationInProgress: LiveData<Boolean>

    fun updateName(name: String?)
    fun updateSurname(surname: String?)
    fun updateEmail(email: String?)
    fun saveUserPersonalInformation()

    fun logOut()

    sealed class NameError{
        object FieldIsRequired: NameError()
        object FieldContainsIllegalSymbols: NameError()
        object StartsWithWhitespace: NameError()
        object EndsWithWhitespace: NameError()
        object RepetitiveWhitespaces: NameError()
        data class LengthIsTooShort(val minLength: Int): NameError()
        data class LengthIsTooLong(val maxLength: Int): NameError()
    }

    sealed class SurnameError{
        object FieldContainsIllegalSymbols: SurnameError()
        object StartsWithWhitespace: SurnameError()
        object EndsWithWhitespace: SurnameError()
        object RepetitiveWhitespaces: SurnameError()
        data class LengthIsTooShort(val minLength: Int): SurnameError()
        data class LengthIsTooLong(val maxLength: Int): SurnameError()
    }

    sealed class EmailError{
        object FieldIsRequired: EmailError()
        object IllegalEmailFormat: EmailError()
        data class LengthIsTooShort(val minLength: Int): EmailError()
        data class LengthIsTooLong(val maxLength: Int): EmailError()
    }
}