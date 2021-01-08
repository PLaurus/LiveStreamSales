package com.example.livestreamsales.viewmodels.authorization

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import com.example.livestreamsales.model.network.rest.error.ResponseError
import com.example.livestreamsales.network.rest.errors.IResponseErrorsManager
import io.reactivex.rxjava3.core.BackpressureStrategy
import javax.inject.Inject

class AuthorizationViewModel @Inject constructor(
    private val responseErrorsManager: IResponseErrorsManager
): ViewModel(), IAuthorizationViewModel {
    override val responseError: LiveData<ResponseError> = LiveDataReactiveStreams.fromPublisher(
        responseErrorsManager.errors.toFlowable(BackpressureStrategy.LATEST)
    )
}