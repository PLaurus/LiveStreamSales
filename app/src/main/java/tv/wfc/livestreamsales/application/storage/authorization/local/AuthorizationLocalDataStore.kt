package tv.wfc.livestreamsales.application.storage.authorization.local

import android.content.SharedPreferences
import androidx.core.content.edit
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.application.di.modules.sharedpreferences.qualifiers.AuthorizationSharedPreferences
import java.util.*
import javax.inject.Inject

class AuthorizationLocalDataStore @Inject constructor(
    @AuthorizationSharedPreferences
    private val authorizationSharedPreferences: SharedPreferences,
    @IoScheduler
    private val ioScheduler: Scheduler
): IAuthorizationLocalDataStore {
    companion object{
        private const val TOKEN_SHARED_PREFERENCES_KEY = "authorization_token"
        private const val NEXT_CODE_REQUEST_DATE_SHARED_PREFERENCES_KEY = "next_code_request_date"
        private const val NEXT_CODE_REQUEST_TIME_ZONE_SHARED_PREFERENCES_KEY = "next_code_request_date_time_zone"
        private const val DEFAULT_NEXT_CODE_REQUEST_REQUIRED_WAITING_TIME = 60L
        private const val DEFAULT_CODE_LENGTH = 4
    }

    private var nextCodeRequestMaxWaitingTime = DEFAULT_NEXT_CODE_REQUEST_REQUIRED_WAITING_TIME
    private var codeLength: Int = DEFAULT_CODE_LENGTH

    override fun getAuthorizationToken(): Maybe<String> = Maybe.fromCallable {
        authorizationSharedPreferences.getString(TOKEN_SHARED_PREFERENCES_KEY, null)
    }

    override fun updateAuthorizationToken(token: String?): Completable {
        return Completable
            .create{ emitter ->
                saveTokenToSharedPreferences(token)
                emitter.onComplete()
            }
            .subscribeOn(ioScheduler)
    }

    override fun getRequiredCodeLength(): Single<Int> {
        return Single
            .just(codeLength)
            .subscribeOn(ioScheduler)
    }

    override fun saveRequiredCodeLength(length: Int): Completable {
        return Completable
            .create { emitter ->
                codeLength = length
                emitter.onComplete()
            }
            .subscribeOn(ioScheduler)
    }

    override fun saveNextCodeRequestMaxWaitingTime(timeInSeconds: Long): Completable {
        return Completable
            .create{ emitter ->
                nextCodeRequestMaxWaitingTime = timeInSeconds
                emitter.onComplete()
            }
            .subscribeOn(ioScheduler)

    }

    override fun getNextCodeRequestMaxWaitingTime(): Single<Long> {
        return Single
            .just(nextCodeRequestMaxWaitingTime)
            .subscribeOn(ioScheduler)
    }

    override fun getNextCodeRequestWaitingTime(): Single<Long> = Single.fromCallable{
        val currentDate = GregorianCalendar.getInstance()

        val nextCodeRequestDateInMillis = authorizationSharedPreferences.getLong(
            NEXT_CODE_REQUEST_DATE_SHARED_PREFERENCES_KEY,
            currentDate.timeInMillis
        )

        val timeZoneId = authorizationSharedPreferences.getString(
            NEXT_CODE_REQUEST_TIME_ZONE_SHARED_PREFERENCES_KEY,
            currentDate.timeZone.id
        )

        val nextCodeRequestDate =
            GregorianCalendar
                .getInstance(TimeZone.getTimeZone(timeZoneId)).apply{
                    timeInMillis = nextCodeRequestDateInMillis
                }

        val waitingTimeInSeconds = ((nextCodeRequestDate.timeInMillis - currentDate.timeInMillis) / 1000)

        waitingTimeInSeconds.coerceIn(0..Long.MAX_VALUE)
    }

    override fun saveNextCodeRequestWaitingTime(leftTimeToWaitInSeconds: Long): Completable = Completable.fromRunnable{
        val currentDate = GregorianCalendar.getInstance()
        val dateWhenCodeRequestIsAvailable = currentDate.timeInMillis + leftTimeToWaitInSeconds * 1000
        val timeZoneId = currentDate.timeZone.id

        authorizationSharedPreferences.edit{
            putLong(NEXT_CODE_REQUEST_DATE_SHARED_PREFERENCES_KEY, dateWhenCodeRequestIsAvailable)
            putString(NEXT_CODE_REQUEST_TIME_ZONE_SHARED_PREFERENCES_KEY, timeZoneId)
        }
    }

    private fun saveTokenToSharedPreferences(token: String?){
        authorizationSharedPreferences.edit(commit = true){
            putString(TOKEN_SHARED_PREFERENCES_KEY, token)
        }
    }
}