package tv.wfc.livestreamsales.application.repository.authorization

import tv.wfc.livestreamsales.features.authorizeduser.di.AuthorizedUserComponent
import tv.wfc.livestreamsales.application.model.phonenumberconfirmation.PhoneNumberConfirmationResult
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface IAuthorizationRepository{
    val isUserLoggedIn: Observable<Boolean>
    val authorizedUserComponent: AuthorizedUserComponent?
    val nextCodeRequestWaitingTime: Observable<Long>
    val isCodeRequestAvailable: Observable<Boolean>
    fun sendConfirmationCodeRequest(phoneNumber: String): Single<Boolean>
    fun getRequiredCodeLength(): Single<Int>
    fun getNextCodeRequestRequiredWaitingTime(): Single<Long>
    fun confirmPhoneNumber(phoneNumber: String, confirmationCode: Int): Single<PhoneNumberConfirmationResult>
    fun logOut(): Completable
}