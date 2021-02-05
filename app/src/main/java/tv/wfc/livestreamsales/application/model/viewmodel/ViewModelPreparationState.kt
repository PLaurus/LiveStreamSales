package tv.wfc.livestreamsales.application.model.viewmodel

sealed class ViewModelPreparationState{
    object DataIsBeingPrepared: ViewModelPreparationState()
    object DataIsPrepared: ViewModelPreparationState()
    object DataIsNotPrepared: ViewModelPreparationState()
    data class FailedToPrepareData(val errorDescription: String? = null): ViewModelPreparationState()
}