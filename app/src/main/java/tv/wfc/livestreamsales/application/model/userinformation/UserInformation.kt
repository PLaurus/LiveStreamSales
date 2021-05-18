package tv.wfc.livestreamsales.application.model.userinformation

data class UserInformation(
    val name: String,
    val surname: String?,
    val phoneNumber: String,
    val email: String?
)