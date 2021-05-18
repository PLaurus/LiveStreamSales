package tv.wfc.livestreamsales.application.storage.userinformation

import tv.wfc.livestreamsales.application.model.userinformation.UserInformation
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single

interface IUserInformationStorage {
    fun getMinUserNameLength(): Single<Int>
    fun getMaxUserNameLength(): Single<Int>
    fun setMinUserNameLength(minLength: Int): Completable
    fun setMaxUserNameLength(maxLength: Int): Completable
    fun getUserInformation(): Maybe<UserInformation>
    fun saveUserInformation(userInformation: UserInformation): Single<Boolean>
}