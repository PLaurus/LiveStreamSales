package tv.wfc.livestreamsales.repository.authorization

import tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.AuthorizedUserComponent
import tv.wfc.livestreamsales.model.application.phonenumberconfirmation.PhoneNumberConfirmationResult
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