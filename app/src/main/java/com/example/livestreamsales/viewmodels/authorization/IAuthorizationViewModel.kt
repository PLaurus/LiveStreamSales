package com.example.livestreamsales.viewmodels.authorization

import androidx.lifecycle.LiveData
import com.example.livestreamsales.model.network.rest.error.ResponseError

interface IAuthorizationViewModel {
    val responseError: LiveData<ResponseError>
}