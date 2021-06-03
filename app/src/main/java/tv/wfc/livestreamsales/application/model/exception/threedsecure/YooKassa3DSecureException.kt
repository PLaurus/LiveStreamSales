package tv.wfc.livestreamsales.application.model.exception.threedsecure

import androidx.annotation.Keep

class YooKassa3DSecureException(
    val errorName: String,
    @Keep
    val errorDescription: String?,
    @Keep
    val errorFailingUrl: String?
): Exception(
    """
        3dSecureError
        Name: $errorName;
        Description: $errorDescription;
        FailingUrl: $errorFailingUrl.
    """.trimIndent()
)