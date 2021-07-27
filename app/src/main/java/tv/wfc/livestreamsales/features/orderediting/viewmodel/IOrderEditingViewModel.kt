package tv.wfc.livestreamsales.features.orderediting.viewmodel

import androidx.lifecycle.LiveData
import com.laurus.p.tools.livedata.LiveEvent
import org.joda.time.DateTime
import tv.wfc.contentloader.viewmodel.INeedPreparationViewModel
import tv.wfc.livestreamsales.application.model.address.Address
import tv.wfc.livestreamsales.application.model.orders.Order
import tv.wfc.livestreamsales.application.model.orders.OrderRecipient
import tv.wfc.livestreamsales.application.model.orders.OrderedProduct

interface IOrderEditingViewModel: INeedPreparationViewModel{
    val isAnyOperationInProgress: LiveData<Boolean>
    val nextDestinationEvent: LiveEvent<NextDestination>
    val orderId: LiveData<Long>
    val orderStatus: LiveData<Order.Status>
    val deliveryCity: LiveData<String>
    val deliveryCityError: LiveData<CityError?>
    val deliveryStreet: LiveData<String>
    val deliveryStreetError: LiveData<StreetError?>
    val deliveryBuilding: LiveData<String>
    val deliveryBuildingError: LiveData<BuildingError?>
    val deliveryFlat: LiveData<String>
    val deliveryFlatError: LiveData<FlatError?>
    val deliveryFloor: LiveData<Int?>
    val deliveryAddress: LiveData<Address?>
    val orderDeliveryDateTime: LiveData<DateTime?>
    val orderRecipient: LiveData<OrderRecipient?>
    val orderSum: LiveData<Float>
    val orderedProducts: LiveData<List<OrderedProduct>>
    val isOrderReadyForConfirmation: LiveData<Boolean>
    val genericErrorEvent: LiveEvent<String>

    fun prepareData(orderId: Long)
    fun updateDeliveryCity(city: String?)
    fun updateDeliveryStreet(street: String?)
    fun updateDeliveryBuilding(building: String?)
    fun updateDeliveryFlat(flat: String?)
    fun updateDeliveryFloor(floor: Int?)
    fun updateDeliveryDate(dateInMillis: Long)
    fun updateDeliveryTime(hour: Int, minute: Int)
    fun intentToCloseCurrentDestination()
    fun intentToNavigateToOrderDeliveryAddressEditing()
    fun intentToNavigateToDeliveryDatePicker()
    fun intentToNavigateToDeliveryTimePicker()
    fun intentToNavigateToOrderConfirmed()
    fun confirmOrder()

    sealed class CityError{
        object FieldIsRequired: CityError()
        object FieldContainsIllegalSymbols: CityError()
    }

    sealed class StreetError{
        object FieldIsRequired: StreetError()
        object FieldContainsIllegalSymbols: StreetError()
    }

    sealed class BuildingError{
        object FieldIsRequired: BuildingError()
        object FieldContainsIllegalSymbols: BuildingError()
    }

    sealed class FlatError{
        object FieldContainsIllegalSymbols: FlatError()
    }

    sealed class NextDestination{
        object Close: NextDestination()
        object OrderDeliveryAddressEditing: NextDestination()
        object DeliveryDatePicker: NextDestination()
        object DeliveryTimePicker: NextDestination()
        object OrderConfirmed: NextDestination()
    }
}