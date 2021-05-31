package tv.wfc.livestreamsales.features.productsareordered.viewmodel

import androidx.lifecycle.LiveData
import tv.wfc.contentloader.viewmodel.INeedPreparationViewModel

interface IProductsAreOrderedViewModel: INeedPreparationViewModel{
    val isAnyOperationInProgress: LiveData<Boolean>
}