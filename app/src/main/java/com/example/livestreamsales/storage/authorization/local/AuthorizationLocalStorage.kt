package com.example.livestreamsales.storage.authorization.local

import com.example.livestreamsales.di.components.app.modules.reactivex.qualifiers.IoScheduler
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class AuthorizationLocalStorage @Inject constructor(
    @IoScheduler
    private val ioScheduler: Scheduler
): IAuthorizationLocalStorage {
    companion object{
        private const val DEFAULT_CODE_LENGTH = 4
    }

    private var codeLength: Int = DEFAULT_CODE_LENGTH

    override fun getCodeLength(): Single<Int> {
        return Single
            .just(codeLength)
            .subscribeOn(ioScheduler)
    }

    override fun setCodeLength(length: Int): Completable {
        return Completable
            .create { emitter ->
                codeLength = length
                emitter.onComplete()
            }
            .subscribeOn(ioScheduler)
    }
}