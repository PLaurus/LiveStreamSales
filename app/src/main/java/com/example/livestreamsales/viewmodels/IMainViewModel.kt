package com.example.livestreamsales.viewmodels

import androidx.lifecycle.LiveData

interface IMainViewModel {
    val isUserLoggedIn: LiveData<Boolean>
}