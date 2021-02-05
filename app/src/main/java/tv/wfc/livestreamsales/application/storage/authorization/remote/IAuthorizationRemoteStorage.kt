package tv.wfc.livestreamsales.application.storage.authorization.remote

import tv.wfc.livestreamsales.application.model.phonenumberconfirmation.PhoneNumberConfirmationResult
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single

interface IAuthorizationRemoteStorage {
    fun sendConfirmationCodeRequest(phoneNumber: String): Single<Boolean>
    fun getRequiredCodeLength(): Maybe<Int>
    fun getNextCodeRequestRequiredWaitingTime(): Maybe<Long>
    fun confirmPhoneNumber(phoneNumber: String, confirmationCode: Int): Single<PhoneNumberConfirmationResult>
}