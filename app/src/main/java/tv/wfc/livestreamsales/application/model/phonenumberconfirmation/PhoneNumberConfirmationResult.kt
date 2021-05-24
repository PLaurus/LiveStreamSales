package tv.wfc.livestreamsales.application.model.phonenumberconfirmation

sealed class PhoneNumberConfirmationResult{
    data class Confirmed(val token: String): PhoneNumberConfirmationResult()
    data class NotConfirmed(val errorMessage: String? = null): PhoneNumberConfirmationResult()
    data class ConfirmedButNeedUserPersonalInformation(val token: String): PhoneNumberConfirmationResult()
}