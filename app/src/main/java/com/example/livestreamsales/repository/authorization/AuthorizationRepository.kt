package com.example.livestreamsales.repository.authorization

import com.example.livestreamsales.di.components.app.ReactiveXModule
import com.example.livestreamsales.model.network.rest.request.SendVerificationCodeRequestRequestBody
import com.example.livestreamsales.network.rest.api.IAuthorizationApi
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject
import javax.inject.Named

class AuthorizationRepository @Inject constructor(
    private val authorizationApi: IAuthorizationApi,
    @Named(ReactiveXModule.DEPENDENCY_NAME_MAIN_THREAD_SCHEDULER)
    private val mainThreadScheduler: Scheduler,
    @Named(ReactiveXModule.DEPENDENCY_NAME_IO_SCHEDULER)
    private val ioScheduler: Scheduler
): IAuthorizationRepository {

    override fun sendVerificationCodeRequest(telephoneNumber: String): Maybe<Boolean> {
        val sendRequestCodeRequestBody = SendVerificationCodeRequestRequestBody(telephoneNumber)
        val response = authorizationApi.sendVerificationCodeRequest(sendRequestCodeRequestBody)

        return response
            .filter{ it.isSuccessful }
            .flatMap{
                val body = it.body()

                if(body != null){
                    Maybe.just(body.isCodeSent)
                } else{
                    Maybe.empty()
                }
            }
    }
}