package tv.wfc.livestreamsales.application.repository.userpersonalinformation

import tv.wfc.livestreamsales.application.model.userpersonalinformation.UserPersonalInformation
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface IUserPersonalInformationRepository {
    fun getMinUserNameLength(): Single<Int>
    fun getMaxUserNameLength(): Single<Int>
    fun updateMinUserNameLength(minLength: Int): Completable
    fun updateMaxUserNameLength(maxLength: Int): Completable

    fun getMinSurnameLength(): Single<Int>
    fun getMaxSurnameLength(): Single<Int>
    fun updateMinSurnameLength(minLength: Int): Completable
    fun updateMaxSurnameLength(maxLength: Int): Completable

    fun getMinEmailLength(): Single<Int>
    fun getMaxEmailLength(): Single<Int>
    fun updateMinEmailLength(minLength: Int): Completable
    fun updateMaxEmailLength(maxLength: Int): Completable

    fun getUserPersonalInformation(): Observable<UserPersonalInformation>
    fun saveUserPersonalInformation(userPersonalInformation: UserPersonalInformation): Completable
}