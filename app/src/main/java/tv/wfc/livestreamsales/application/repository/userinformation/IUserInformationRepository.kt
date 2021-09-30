package tv.wfc.livestreamsales.application.repository.userinformation

import tv.wfc.livestreamsales.application.model.user.User
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface IUserInformationRepository {
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

    fun getUserInformation(): Observable<User>
    fun saveUserInformation(user: User): Completable
}