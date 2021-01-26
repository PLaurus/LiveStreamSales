package com.example.livestreamsales.model.application.viewmodel

sealed class ViewModelPreparationState{
    object DataIsBeingPrepared: ViewModelPreparationState()
    object DataIsPrepared: ViewModelPreparationState()
    object DataIsNotPrepared: ViewModelPreparationState()
    data class FailedToPrepareData(val errorDescription: String? = null): ViewModelPreparationState()
}