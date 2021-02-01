package com.example.livestreamsales.storage.userinformation.local

import com.example.livestreamsales.di.components.app.modules.reactivex.qualifiers.IoScheduler
import com.example.livestreamsales.model.application.user.UserInformation
import com.example.livestreamsales.storage.userinformation.IUserInformationStorage
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class UserInformationLocalStorage @Inject constructor(
    @IoScheduler
    private val ioScheduler: Scheduler
): IUserInformationStorage {
    companion object{
        private const val DEFAULT_MIN_USER_NAME_LENGTH = 5
        private const val DEFAULT_MAX_USER_NAME_LENGTH = 15
    }

    private var userInformation: UserInformation? = null
    private var minUserNameLength: Int = DEFAULT_MIN_USER_NAME_LENGTH
    private var maxUserNameLength: Int = DEFAULT_MAX_USER_NAME_LENGTH

    override fun getMinUserNameLength(): Single<Int> {
        return Single
            .just(minUserNameLength)
            .subscribeOn(ioScheduler)
    }

    override fun getMaxUserNameLength(): Single<Int> {
        return Single
            .just(maxUserNameLength)
            .subscribeOn(ioScheduler)
    }

    override fun setMinUserNameLength(minLength: Int): Completable {
        return Completable
            .create { emitter ->
                minUserNameLength = minLength
                emitter.onComplete()
            }
            .subscribeOn(ioScheduler)
    }

    override fun setMaxUserNameLength(maxLength: Int): Completable {
        return Completable
            .create { emitter ->
                maxUserNameLength = maxLength
                emitter.onComplete()
            }
            .subscribeOn(ioScheduler)
    }

    override fun getUserInformation(): Maybe<UserInformation> {
        val userInformationTemp = userInformation

        val result = if(userInformationTemp != null){
            Maybe.just(userInformationTemp)
        } else Maybe.empty()

        return result
            .subscribeOn(ioScheduler)
    }

    override fun saveUserInformation(userInformation: UserInformation): Single<Boolean> {
        return Single
            .create<Boolean> { emitter ->
                this.userInformation = userInformation
                emitter.onSuccess(true)
            }
            .subscribeOn(ioScheduler)
    }
}