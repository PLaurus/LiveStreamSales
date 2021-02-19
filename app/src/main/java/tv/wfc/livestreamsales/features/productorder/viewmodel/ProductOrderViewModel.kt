package tv.wfc.livestreamsales.features.productorder.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tv.wfc.contentloader.model.ViewModelPreparationState
import javax.inject.Inject

class ProductOrderViewModel @Inject constructor(

): ViewModel(), IProductOrderViewModel {
    override val dataPreparationState = MutableLiveData<ViewModelPreparationState>(ViewModelPreparationState.DataIsPrepared)
}