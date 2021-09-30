package tv.wfc.livestreamsales.application.storage.authorization.local

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single

interface IAuthorizationLocalDataStore {
    fun getAuthorizationToken(): Maybe<String>
    fun updateAuthorizationToken(token: String?): Completable
    fun getRequiredCodeLength(): Single<Int>
    fun saveRequiredCodeLength(length: Int): Completable
    fun saveNextCodeRequestMaxWaitingTime(timeInSeconds: Long): Completable
    fun getNextCodeRequestMaxWaitingTime(): Single<Long>
    fun getNextCodeRequestWaitingTime(): Single<Long>
    fun saveNextCodeRequestWaitingTime(leftTimeToWaitInSeconds: Long): Completable
}