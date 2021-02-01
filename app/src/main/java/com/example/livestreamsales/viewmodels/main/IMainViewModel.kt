package com.example.livestreamsales.viewmodels.main

import androidx.lifecycle.LiveData

interface IMainViewModel {
    val isUserLoggedIn: LiveData<Boolean>
}