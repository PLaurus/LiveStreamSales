package tv.wfc.livestreamsales.repository.userinformation

import tv.wfc.livestreamsales.model.application.user.UserInformation
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface IUserInformationRepository {
    fun getMinUserNameLength(): Single<Int>
    fun getMaxUserNameLength(): Single<Int>
    fun setMinUserNameLength(minLength: Int): Completable
    fun setMaxUserNameLength(maxLength: Int): Completable
    fun getUserInformation(): Observable<UserInformation>
    fun saveUserInformation(userInformation: UserInformation): Single<Boolean>
}