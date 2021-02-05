package tv.wfc.livestreamsales.application.model.phonenumberconfirmation

sealed class PhoneNumberConfirmationResult{
    data class PhoneNumberIsConfirmed(val token: String): PhoneNumberConfirmationResult()
    data class PhoneNumberIsNotConfirmed(val errorMessage: String? = null): PhoneNumberConfirmationResult()
}