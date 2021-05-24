package tv.wfc.livestreamsales.application.manager

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import tv.wfc.livestreamsales.application.model.phonenumberconfirmation.PhoneNumberConfirmationResult

interface IAuthorizationManager {
    val isUserLoggedIn: Observable<Boolean>
    val nextCodeRequestWaitingTime: Observable<Long>
    val isCodeRequestAvailable: Observable<Boolean>

    fun checkIsUserLoggedIn(): Boolean
    fun requestConfirmationCode(phoneNumber: String): Single<Boolean>
    fun confirmPhoneNumber(phoneNumber: String, confirmationCode: Int): Single<PhoneNumberConfirmationResult>
    fun logInTemporary(token: String): Completable
    fun logIn(token: String): Completable
    fun logOut(): Completable
}