package tv.wfc.livestreamsales.model.application.user

data class UserInformation(
    val name: String,
    val surname: String?,
    val phoneNumber: String,
    val email: String?
)