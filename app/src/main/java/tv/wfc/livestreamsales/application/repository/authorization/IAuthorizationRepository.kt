package tv.wfc.livestreamsales.application.repository.authorization

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import tv.wfc.livestreamsales.application.model.phonenumberconfirmation.PhoneNumberConfirmationResult

interface IAuthorizationRepository{
    fun getCurrentAuthorizationToken(): Maybe<String>
    fun updateAuthorizationToken(token: String?): Completable
    fun requestConfirmationCode(phoneNumber: String): Single<Boolean>
    fun getRequiredCodeLength(): Single<Int>
    fun getNextCodeRequestRequiredWaitingTime(): Single<Long>
    fun getNextCodeRequestWaitingTime(): Single<Long>
    fun saveNextCodeRequestWaitingTime(leftTimeToWaitInSeconds: Long): Completable
    fun confirmPhoneNumber(phoneNumber: String, confirmationCode: Int): Single<PhoneNumberConfirmationResult>
    fun logOut(): Completable
}