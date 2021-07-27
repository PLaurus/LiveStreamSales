package tv.wfc.livestreamsales.features.orderediting.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.laurus.p.tools.livedata.LiveEvent
import com.laurus.p.tools.reactivex.NullablesWrapper
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.BehaviorSubject
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import tv.wfc.contentloader.model.ViewModelPreparationState
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.application.manager.IAuthorizationManager
import tv.wfc.livestreamsales.application.model.address.Address
import tv.wfc.livestreamsales.application.model.orders.Order
import tv.wfc.livestreamsales.application.model.orders.OrderRecipient
import tv.wfc.livestreamsales.application.model.orders.OrderedProduct
import tv.wfc.livestreamsales.application.repository.productsorder.IProductsOrderRepository
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import java.util.regex.Pattern
import javax.inject.Inject

class OrderEditingViewModel @Inject constructor(
    private val applicationContext: Context,
    private val authorizationManager: IAuthorizationManager,
    private val productsOrderRepository: IProductsOrderRepository,
    @MainThreadScheduler
    private val mainThreadScheduler: Scheduler,
    private val applicationErrorsLogger: IApplicationErrorsLogger
): ViewModel(), IOrderEditingViewModel {
    private val disposables = CompositeDisposable()
    private val activeOperationsCount = BehaviorSubject.createDefault(0)
    private val deliveryCityErrorSubject = BehaviorSubject.create<NullablesWrapper<IOrderEditingViewModel.CityError>>()
    private val deliveryStreetErrorSubject = BehaviorSubject.create<NullablesWrapper<IOrderEditingViewModel.StreetError>>()
    private val deliveryBuildingErrorSubject = BehaviorSubject.create<NullablesWrapper<IOrderEditingViewModel.BuildingError>>()
    private val deliveryFlatErrorSubject = BehaviorSubject.create<NullablesWrapper<IOrderEditingViewModel.FlatError>>()

    private var dataPreparationDisposable: Disposable? = null
    private var orderConfirmationDisposable: Disposable? = null
    private var isDataBeingConfirmed: Boolean = false

    override val dataPreparationState = MutableLiveData<ViewModelPreparationState>(ViewModelPreparationState.DataIsNotPrepared)

    override val isAnyOperationInProgress = MutableLiveData<Boolean>().apply {
        activeOperationsCount
            .observeOn(mainThreadScheduler)
            .map { it > 0 }
            .subscribeBy(
                onNext = ::setValue,
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val nextDestinationEvent = LiveEvent<IOrderEditingViewModel.NextDestination>()
    override val orderId = MutableLiveData<Long>()
    override val orderStatus = MutableLiveData<Order.Status>()
    override val deliveryCity = MutableLiveData<String>()

    override val deliveryCityError: LiveData<IOrderEditingViewModel.CityError?> =
        MutableLiveData<IOrderEditingViewModel.CityError?>().apply{
            deliveryCityErrorSubject
                .observeOn(mainThreadScheduler)
                .subscribeBy(
                    onNext = { value = it.value },
                    onError = applicationErrorsLogger::logError
                )
                .addTo(disposables)
        }

    override val deliveryStreet = MutableLiveData<String>()

    override val deliveryStreetError: LiveData<IOrderEditingViewModel.StreetError?> =
        MutableLiveData<IOrderEditingViewModel.StreetError?>().apply{
            deliveryStreetErrorSubject
                .observeOn(mainThreadScheduler)
                .subscribeBy(
                    onNext = { value = it.value },
                    onError = applicationErrorsLogger::logError
                )
                .addTo(disposables)
        }

    override val deliveryBuilding = MutableLiveData<String>()

    override val deliveryBuildingError: LiveData<IOrderEditingViewModel.BuildingError?> =
        MutableLiveData<IOrderEditingViewModel.BuildingError?>().apply{
            deliveryBuildingErrorSubject
                .observeOn(mainThreadScheduler)
                .subscribeBy(
                    onNext = { value = it.value },
                    onError = applicationErrorsLogger::logError
                )
                .addTo(disposables)
        }

    override val deliveryFlat = MutableLiveData<String>()

    override val deliveryFlatError: LiveData<IOrderEditingViewModel.FlatError?> =
        MutableLiveData<IOrderEditingViewModel.FlatError?>().apply{
            deliveryFlatErrorSubject
                .observeOn(mainThreadScheduler)
                .subscribeBy(
                    onNext = { value = it.value },
                    onError = applicationErrorsLogger::logError
                )
                .addTo(disposables)
        }

    override val deliveryFloor = MutableLiveData<Int?>()

    override val deliveryAddress = MediatorLiveData<Address?>().apply{
        val sources = listOf(
            deliveryCity,
            deliveryStreet,
            deliveryBuilding,
            deliveryFlat,
            deliveryFloor
        )

        val onChanged: (Any?) -> Unit = { value = makeAddressFromFields() }

        sources.forEach{ addSource(it, onChanged) }
    }

    override val orderDeliveryDateTime = MutableLiveData<DateTime?>()
    override val orderRecipient = MutableLiveData<OrderRecipient?>()
    override val orderSum = MutableLiveData<Float>()
    override val orderedProducts = MutableLiveData<List<OrderedProduct>>()

    override val isOrderReadyForConfirmation = MediatorLiveData<Boolean>().apply{
        val sources = listOf(
            deliveryAddress
        )

        val onChanged: (Any?) -> Unit = {
            value = sources
                .map { it.value != null }
                .reduce { result, next -> result and next }
        }

        sources.forEach { addSource(it, onChanged) }
    }

    override val genericErrorEvent = LiveEvent<String>()

    init{
        closeWhenNotAuthorized()
    }

    override fun prepareData(orderId: Long) {
        if(dataPreparationState.value == ViewModelPreparationState.DataIsPrepared) return

        dataPreparationDisposable?.dispose()

        dataPreparationDisposable = Completable
            .mergeArray(
                prepareOrderInformation(orderId)
            )
            .observeOn(mainThreadScheduler)
            .doOnSubscribe { dataPreparationState.value = ViewModelPreparationState.DataIsBeingPrepared }
            .subscribeBy(
                onComplete = { dataPreparationState.value = ViewModelPreparationState.DataIsPrepared },
                onError = {
                    dataPreparationState.value = ViewModelPreparationState.FailedToPrepareData(it.message)
                    applicationErrorsLogger.logError(it)
                }
            )
            .addTo(disposables)
    }

    override fun updateDeliveryCity(city: String?) {
        deliveryCityErrorSubject.onNext(NullablesWrapper(checkDeliveryCity(city)))
        if(this.deliveryCity.value == city) return
        this.deliveryCity.value = city
    }

    override fun updateDeliveryStreet(street: String?) {
        deliveryStreetErrorSubject.onNext(NullablesWrapper(checkDeliveryStreet(street)))
        if(this.deliveryStreet.value == street) return
        this.deliveryStreet.value = street
    }

    override fun updateDeliveryBuilding(building: String?) {
        deliveryBuildingErrorSubject.onNext(NullablesWrapper(checkDeliveryBuilding(building)))
        if(this.deliveryBuilding.value == building) return
        this.deliveryBuilding.value = building
    }

    override fun updateDeliveryFlat(flat: String?) {
        deliveryFlatErrorSubject.onNext(NullablesWrapper(checkDeliveryFlat(flat)))
        if(this.deliveryFlat.value == flat) return
        this.deliveryFlat.value = flat
    }

    override fun updateDeliveryFloor(floor: Int?) {
        if(this.deliveryFloor.value == floor) return
        this.deliveryFloor.value = floor
    }

    override fun updateDeliveryDate(dateInMillis: Long) {
        incrementActiveOperationsCount()
        val existingDeliveryTime = orderDeliveryDateTime.value?.toLocalTime()
        val newDeliveryDateTime = DateTime(dateInMillis, DateTimeZone.UTC)
        orderDeliveryDateTime.value = existingDeliveryTime?.let(newDeliveryDateTime::withTime) ?: newDeliveryDateTime
        decrementActiveOperationsCount()
    }

    override fun updateDeliveryTime(hour: Int, minute: Int) {
        incrementActiveOperationsCount()
        val existingDeliveryDateTime = orderDeliveryDateTime.value
        if(existingDeliveryDateTime != null){
            orderDeliveryDateTime.value = existingDeliveryDateTime
                .withTime(hour, minute, /* second = */0, /* millisecond = */0)
        }
        decrementActiveOperationsCount()
    }

    override fun intentToCloseCurrentDestination() {
        incrementActiveOperationsCount()
        nextDestinationEvent.value = IOrderEditingViewModel.NextDestination.Close
        decrementActiveOperationsCount()
    }

    override fun intentToNavigateToOrderDeliveryAddressEditing() {
        incrementActiveOperationsCount()
        nextDestinationEvent.value = IOrderEditingViewModel.NextDestination.OrderDeliveryAddressEditing
        decrementActiveOperationsCount()
    }

    override fun intentToNavigateToDeliveryDatePicker() {
        incrementActiveOperationsCount()
        nextDestinationEvent.value = IOrderEditingViewModel.NextDestination.DeliveryDatePicker
        decrementActiveOperationsCount()
    }

    override fun intentToNavigateToDeliveryTimePicker() {
        incrementActiveOperationsCount()
        nextDestinationEvent.value = IOrderEditingViewModel.NextDestination.DeliveryTimePicker
        decrementActiveOperationsCount()
    }

    override fun intentToNavigateToOrderConfirmed() {
        incrementActiveOperationsCount()
        nextDestinationEvent.value = IOrderEditingViewModel.NextDestination.OrderConfirmed
        decrementActiveOperationsCount()
    }

    @Synchronized
    override fun confirmOrder() {
        if(isDataBeingConfirmed) return
        val orderId = orderId.value ?: return
        val deliveryAddress = this.deliveryAddress.value ?: return
        val deliveryDate = this.orderDeliveryDateTime.value

        orderConfirmationDisposable?.dispose()
        orderConfirmationDisposable = productsOrderRepository
            .confirmOrder(orderId, deliveryAddress, deliveryDate)
            .observeOn(mainThreadScheduler)
            .doOnSubscribe{ incrementActiveOperationsCount() }
            .doOnTerminate(::decrementActiveOperationsCount)
            .subscribeBy(
                onSuccess = {
                    if(it.isUpdated){
                        intentToNavigateToOrderConfirmed()
                    }

                    it.message?.let(genericErrorEvent::setValue)
                },
                onError = {
                    applicationErrorsLogger.logError(it)

                    val failedToConfirmOrderMessage = applicationContext.getString(R.string.fragment_order_editing_error_failed_to_confirm_order)
                    genericErrorEvent.value = failedToConfirmOrderMessage
                }
            )
            .addTo(disposables)
    }

    private fun prepareOrderInformation(orderId: Long): Completable{
        return productsOrderRepository
            .getOrder(orderId)
            .observeOn(mainThreadScheduler)
            .flatMapCompletable { order ->
                Completable.fromRunnable {
                    this.orderId.value = order.id
                    orderStatus.value = order.status
                    deliveryAddress.value = order.deliveryAddress
                    updateDeliveryCity(order.deliveryAddress?.city)
                    updateDeliveryStreet(order.deliveryAddress?.street)
                    updateDeliveryBuilding(order.deliveryAddress?.building)
                    updateDeliveryFlat(order.deliveryAddress?.flat)
                    updateDeliveryFloor(order.deliveryAddress?.floor)

                    val currentTimeZone = DateTimeZone.getDefault()
                    orderDeliveryDateTime.value = order.deliveryDate?.withZone(currentTimeZone)

                    orderRecipient.value = order.orderRecipient
                    orderSum.value = order.orderPrice
                    orderedProducts.value = order.products
                }
            }
    }

    private fun closeWhenNotAuthorized(){
        authorizationManager
            .isUserLoggedIn
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { isUserAuthorized ->
                    if(!isUserAuthorized) {
                        nextDestinationEvent.value = IOrderEditingViewModel.NextDestination.Close
                    }
                },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    private fun checkDeliveryCity(city: String?): IOrderEditingViewModel.CityError?{
        if(city.isNullOrEmpty()) return IOrderEditingViewModel.CityError.FieldIsRequired
        if(!city.checkContainsOnlySingleLanguageLetters()) return IOrderEditingViewModel.CityError.FieldContainsIllegalSymbols

        return null
    }

    private fun checkDeliveryStreet(street: String?): IOrderEditingViewModel.StreetError?{
        if(street.isNullOrEmpty()) return IOrderEditingViewModel.StreetError.FieldIsRequired
        if(!street.checkContainsOnlySingleLanguageLetters()) return IOrderEditingViewModel.StreetError.FieldContainsIllegalSymbols

        return null
    }

    private fun checkDeliveryBuilding(building: String?): IOrderEditingViewModel.BuildingError?{
        if(building.isNullOrEmpty()) return IOrderEditingViewModel.BuildingError.FieldIsRequired
        if(!building.checkContainsOnlySingleLanguageLetters()) return IOrderEditingViewModel.BuildingError.FieldContainsIllegalSymbols

        return null
    }

    private fun checkDeliveryFlat(flat: String?): IOrderEditingViewModel.FlatError?{
        if(flat?.checkContainsOnlySingleLanguageLetters() == false) {
            return IOrderEditingViewModel.FlatError.FieldContainsIllegalSymbols
        }

        return null
    }

    private fun checkAddressFieldsContainErrors(): Boolean{
        return deliveryCityErrorSubject.value.value != null ||
                deliveryStreetErrorSubject.value.value != null ||
                deliveryBuildingErrorSubject.value.value != null ||
                deliveryFlatErrorSubject.value.value != null
    }

    private fun String.checkContainsOnlySingleLanguageLetters(): Boolean{
        return Pattern.matches("^([а-яА-яёЁ,. \\-()0-9]*|[a-zA-Z,. \\-()0-9]*)$", this)
    }

    private fun makeAddressFromFields(): Address?{
        if(checkAddressFieldsContainErrors()) return null
        val city = this.deliveryCity.value ?: return null
        val street = this.deliveryStreet.value ?: return null
        val building = this.deliveryBuilding.value ?: return null

        return Address(
            city,
            street,
            building,
            flat = deliveryFlat.value,
            floor = deliveryFloor.value
        )
    }

    @Synchronized
    private fun incrementActiveOperationsCount(){
        val currentActiveOperationsCount = activeOperationsCount.value ?: 0
        val newActiveOperationsCount = currentActiveOperationsCount + 1

        activeOperationsCount.onNext(newActiveOperationsCount)
    }

    @Synchronized
    private fun decrementActiveOperationsCount(){
        val currentActiveOperationsCount = activeOperationsCount.value ?: 0
        val newActiveOperationsCount = currentActiveOperationsCount - 1

        activeOperationsCount.onNext(newActiveOperationsCount)
    }
}