package com.example.livestreamsales.network.rest

import com.example.livestreamsales.authorization.IAuthorizationManager
import com.example.livestreamsales.network.rest.api.base.IApi
import com.example.livestreamsales.network.rest.api.base.IAuthorizedApi
import com.example.livestreamsales.network.rest.interceptors.AuthorizationInterceptor
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Inject

class RetrofitRestManager @Inject constructor(
    private val baseOkHttpClient: OkHttpClient,
    private val baseRetrofit: Retrofit,
    private val mainThreadScheduler: Scheduler,
    authorizationManager: IAuthorizationManager
): IRestManager {
    private val disposables = CompositeDisposable()

    private var authorizedRetrofit: Retrofit? = null

    init{
        observeAuthorizationToken(authorizationManager.token)
    }

    override fun <T: IApi> createApi(apiClass: Class<T>): T{
        return baseRetrofit.create(apiClass)
    }

    override fun <T: IAuthorizedApi> createAuthorizedApi(apiClass: Class<T>): T?{
        return authorizedRetrofit?.create(apiClass)
    }

    private fun observeAuthorizationToken(tokenObservable: Observable<String?>){
        tokenObservable
                .observeOn(mainThreadScheduler)
                .subscribe(::updateAuthorizedRetrofit)
                .addTo(disposables)
    }

    private fun updateAuthorizedRetrofit(authorizationToken: String?){
        authorizedRetrofit = if(authorizationToken != null){
            val authorizationInterceptor = AuthorizationInterceptor(authorizationToken)
            val authorizedOkHttpClient = getAuthorizedOkHttpClient(authorizationInterceptor)

            baseRetrofit.newBuilder()
                    .client(authorizedOkHttpClient)
                    .build()
        } else null
    }

    private fun getAuthorizedOkHttpClient(
        authorizationInterceptor: AuthorizationInterceptor
    ): OkHttpClient{
        return baseOkHttpClient.newBuilder()
                .addInterceptor(authorizationInterceptor)
                .build()
    }
}