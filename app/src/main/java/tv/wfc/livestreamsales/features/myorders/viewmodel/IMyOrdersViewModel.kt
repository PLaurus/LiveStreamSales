package tv.wfc.livestreamsales.features.myorders.viewmodel

import androidx.lifecycle.LiveData
import com.laurus.p.tools.livedata.LiveEvent
import tv.wfc.contentloader.viewmodel.INeedPreparationViewModel
import tv.wfc.livestreamsales.application.model.orders.Order

interface IMyOrdersViewModel: INeedPreparationViewModel {
    val isDataBeingRefreshed: LiveData<Boolean>
    val isAnyOperationInProgress: LiveData<Boolean>
    val nextDestinationEvent: LiveEvent<NextDestination>

    val orders: LiveData<List<Order>>

    fun refreshData()

    fun intentToNavigateToOrderEditing(orderId: Long)
    fun intentToNavigateToOrderInformation(orderId: Long)
    fun intentToCloseCurrentDestination()

    sealed class NextDestination{
        data class OrderEditing(val orderId: Long): NextDestination()
        object NeedPaymentInformation: NextDestination()
        data class OrderInformation(val orderId: Long): NextDestination()
        object Close: NextDestination()
    }
}