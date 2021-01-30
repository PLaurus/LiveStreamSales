package com.example.livestreamsales.viewmodels.base

import androidx.lifecycle.LiveData
import com.example.livestreamsales.model.application.viewmodel.ViewModelPreparationState

interface IToBePreparedViewModel {
    val dataPreparationState: LiveData<ViewModelPreparationState>
}