package tv.wfc.livestreamsales.features.authorization.paymentcardinformation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import tv.wfc.contentloader.model.ViewModelPreparationState
import javax.inject.Inject

class RegistrationPaymentCardInformationViewModel @Inject constructor(

): ViewModel(), IRegistrationPaymentCardInformationViewModel {
    private val disposables = CompositeDisposable()

    override val dataPreparationState = MutableLiveData<ViewModelPreparationState>(ViewModelPreparationState.DataIsPrepared)

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }
}