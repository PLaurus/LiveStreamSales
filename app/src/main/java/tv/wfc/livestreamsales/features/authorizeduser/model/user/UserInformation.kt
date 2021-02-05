package tv.wfc.livestreamsales.features.authorizeduser.model.user

data class UserInformation(
    val name: String,
    val surname: String?,
    val phoneNumber: String,
    val email: String?
)