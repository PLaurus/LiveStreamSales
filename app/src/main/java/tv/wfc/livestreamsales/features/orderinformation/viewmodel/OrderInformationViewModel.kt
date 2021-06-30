package tv.wfc.livestreamsales.features.orderinformation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.laurus.p.tools.livedata.LiveEvent
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
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.application.manager.IAuthorizationManager
import tv.wfc.livestreamsales.application.model.address.Address
import tv.wfc.livestreamsales.application.model.orders.Order
import tv.wfc.livestreamsales.application.model.orders.OrderRecipient
import tv.wfc.livestreamsales.application.model.orders.OrderedProduct
import tv.wfc.livestreamsales.application.repository.productsorder.IProductsOrderRepository
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import javax.inject.Inject

class OrderInformationViewModel @Inject constructor(
    private val authorizationManager: IAuthorizationManager,
    private val productsOrderRepository: IProductsOrderRepository,
    @MainThreadScheduler
    private val mainThreadScheduler: Scheduler,
    private val applicationErrorsLogger: IApplicationErrorsLogger
): ViewModel(), IOrderInformationViewModel {
    private val disposables = CompositeDisposable()
    private val activeOperationsCount = BehaviorSubject.createDefault(0)
    private var dataPreparationDisposable: Disposable? = null

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
    override val nextDestinationEvent = LiveEvent<IOrderInformationViewModel.NextDestination>()
    override val orderId = MutableLiveData<Long>()
    override val orderStatus = MutableLiveData<Order.Status>()
    override val orderCreationDate = MutableLiveData<DateTime>()
    override val orderDeliveryDate = MutableLiveData<DateTime?>()
    override val deliveryAddress = MutableLiveData<Address?>()
    override val orderRecipient = MutableLiveData<OrderRecipient?>()
    override val orderSum = MutableLiveData<Float>()
    override val orderedProducts = MutableLiveData<List<OrderedProduct>>()

    init{
        closeWhenNotAuthorized()
    }

    override fun prepareData(orderId: Long){
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

    override fun intentToCloseCurrentDestination() {
        incrementActiveOperationsCount()
        nextDestinationEvent.value = IOrderInformationViewModel.NextDestination.Close
        decrementActiveOperationsCount()
    }

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }

    private fun prepareOrderInformation(orderId: Long): Completable{
        return productsOrderRepository
            .getOrder(orderId)
            .observeOn(mainThreadScheduler)
            .flatMapCompletable { order ->
                Completable.fromRunnable {
                    this.orderId.value = order.id
                    orderStatus.value = order.status

                    val currentTimeZone = DateTimeZone.getDefault()
                    orderCreationDate.value = order.orderDate.withZone(currentTimeZone)
                    orderDeliveryDate.value = order.deliveryDate?.withZone(currentTimeZone)

                    deliveryAddress.value = order.deliveryAddress
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
                        nextDestinationEvent.value = IOrderInformationViewModel.NextDestination.Close
                    }
                },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
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