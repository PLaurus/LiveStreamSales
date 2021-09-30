package tv.wfc.livestreamsales.application.storage.userpersonalinformation.local

import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.application.model.user.User
import tv.wfc.livestreamsales.application.storage.userpersonalinformation.IUserInformationDataStore
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import tv.wfc.livestreamsales.application.model.exception.storage.NoSuchDataInStorageException
import javax.inject.Inject

class UserInformationLocalDataStore @Inject constructor(
    @IoScheduler
    private val ioScheduler: Scheduler
): IUserInformationDataStore {
    companion object{
        private const val DEFAULT_MIN_USER_NAME_LENGTH = 3
        private const val DEFAULT_MAX_USER_NAME_LENGTH = 20
        private const val DEFAULT_MIN_SURNAME_LENGTH = 0
        private const val DEFAULT_MAX_SURNAME_LENGTH = 30
        private const val DEFAULT_MIN_EMAIL_LENGTH = 6
        private const val DEFAULT_MAX_EMAIL_LENGTH = 40
    }

    private var user: User? = null
    private var minUserNameLength: Int = DEFAULT_MIN_USER_NAME_LENGTH
    private var maxUserNameLength: Int = DEFAULT_MAX_USER_NAME_LENGTH
    private var minSurnameLength: Int = DEFAULT_MIN_SURNAME_LENGTH
    private var maxSurnameLength: Int = DEFAULT_MAX_SURNAME_LENGTH
    private var minEmailLength: Int = DEFAULT_MIN_EMAIL_LENGTH
    private var maxEmailLength: Int = DEFAULT_MAX_EMAIL_LENGTH

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

    override fun updateMinUserNameLength(minLength: Int): Completable {
        return Completable
            .create { emitter ->
                minUserNameLength = minLength
                emitter.onComplete()
            }
            .subscribeOn(ioScheduler)
    }

    override fun updateMaxUserNameLength(maxLength: Int): Completable {
        return Completable
            .create { emitter ->
                maxUserNameLength = maxLength
                emitter.onComplete()
            }
            .subscribeOn(ioScheduler)
    }

    override fun getMinSurnameLength(): Single<Int> {
        return Single
            .just(minSurnameLength)
            .subscribeOn(ioScheduler)
    }

    override fun getMaxSurnameLength(): Single<Int> {
        return Single
            .just(maxSurnameLength)
            .subscribeOn(ioScheduler)
    }

    override fun updateMinSurnameLength(minLength: Int): Completable {
        return Completable
            .fromRunnable {
                minUserNameLength = minLength
            }
            .subscribeOn(ioScheduler)
    }

    override fun updateMaxSurnameLength(maxLength: Int): Completable {
        return Completable
            .fromRunnable{
                maxSurnameLength = maxLength
            }
            .subscribeOn(ioScheduler)
    }

    override fun getMinEmailLength(): Single<Int> {
        return Single
            .just(minEmailLength)
            .subscribeOn(ioScheduler)
    }

    override fun getMaxEmailLength(): Single<Int> {
        return Single
            .just(maxEmailLength)
            .subscribeOn(ioScheduler)
    }

    override fun updateMinEmailLength(minLength: Int): Completable {
        return Completable
            .fromRunnable {
                minEmailLength = minLength
            }
            .subscribeOn(ioScheduler)
    }

    override fun updateMaxEmailLength(maxLength: Int): Completable {
        return Completable
            .fromRunnable {
                maxEmailLength = maxLength
            }
            .subscribeOn(ioScheduler)
    }

    override fun getUserInformation(): Single<User> {
        val userInformationTemp = user

        val result = if(userInformationTemp != null){
            Single.just(userInformationTemp)
        } else Single.error(NoSuchDataInStorageException())

        return result
            .subscribeOn(ioScheduler)
    }

    override fun saveUserInformation(user: User): Completable {
        return Completable
            .fromRunnable {
                this.user = user
            }
            .subscribeOn(ioScheduler)
    }
}