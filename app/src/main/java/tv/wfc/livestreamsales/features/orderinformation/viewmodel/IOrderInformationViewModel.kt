package tv.wfc.livestreamsales.features.orderinformation.viewmodel

import androidx.lifecycle.LiveData
import com.laurus.p.tools.livedata.LiveEvent
import org.joda.time.DateTime
import tv.wfc.contentloader.viewmodel.INeedPreparationViewModel
import tv.wfc.livestreamsales.application.model.address.Address
import tv.wfc.livestreamsales.application.model.orders.Order
import tv.wfc.livestreamsales.application.model.orders.OrderRecipient
import tv.wfc.livestreamsales.application.model.orders.OrderedProduct

interface IOrderInformationViewModel: INeedPreparationViewModel {
    val isAnyOperationInProgress: LiveData<Boolean>
    val nextDestinationEvent: LiveEvent<NextDestination>
    val orderId: LiveData<Long>
    val orderStatus: LiveData<Order.Status>
    val orderCreationDate: LiveData<DateTime>
    val orderDeliveryDate: LiveData<DateTime?>
    val deliveryAddress: LiveData<Address?>
    val orderRecipient: LiveData<OrderRecipient?>
    val orderSum: LiveData<Float>
    val orderedProducts: LiveData<List<OrderedProduct>>

    fun prepareData(orderId: Long)
    fun intentToCloseCurrentDestination()

    sealed class NextDestination{
        object Close: NextDestination()
    }
}