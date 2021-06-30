package tv.wfc.livestreamsales.features.myorders.viewmodel

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
import tv.wfc.contentloader.model.ViewModelPreparationState
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.application.manager.IAuthorizationManager
import tv.wfc.livestreamsales.application.model.orders.Order
import tv.wfc.livestreamsales.application.repository.paymentcardinformation.IPaymentCardInformationRepository
import tv.wfc.livestreamsales.application.repository.productsorder.IProductsOrderRepository
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import javax.inject.Inject

class MyOrdersViewModel @Inject constructor(
    private val authorizationManager: IAuthorizationManager,
    private val paymentCardInformationRepository: IPaymentCardInformationRepository,
    private val productsOrderRepository: IProductsOrderRepository,
    @MainThreadScheduler
    private val mainThreadScheduler: Scheduler,
    private val applicationErrorsLogger: IApplicationErrorsLogger
): ViewModel(), IMyOrdersViewModel {
    private val disposables = CompositeDisposable()
    private val activeOperationsCount = BehaviorSubject.createDefault(0)

    private var dataPreparationDisposable: Disposable? = null
    private var dataRefreshmentDisposable: Disposable? = null
    private var checkIsOrderEditingAvailableDisposable: Disposable? = null

    override val dataPreparationState = MutableLiveData<ViewModelPreparationState>(ViewModelPreparationState.DataIsNotPrepared)
    override val isDataBeingRefreshed = MutableLiveData<Boolean>()

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
    override val nextDestinationEvent = LiveEvent<IMyOrdersViewModel.NextDestination>()

    override val orders = MutableLiveData<List<Order>>()

    override fun refreshData() {
        dataRefreshmentDisposable?.dispose()

        dataRefreshmentDisposable = Completable
            .mergeArray(
                prepareOrders()
            )
            .observeOn(mainThreadScheduler)
            .doOnSubscribe { isDataBeingRefreshed.value = true }
            .doOnTerminate { isDataBeingRefreshed.value = false }
            .subscribeBy(applicationErrorsLogger::logError)
            .addTo(disposables)
    }

    @Synchronized
    override fun intentToNavigateToOrderEditing(orderId: Long) {
        val orderStatus = orders.value?.first{ it.id == orderId }?.status ?: return

        if(orderStatus != Order.Status.JUST_MADE) return

        checkIsOrderEditingAvailableDisposable?.dispose()
        checkIsOrderEditingAvailableDisposable = paymentCardInformationRepository
            .getPaymentCardInformation()
            .map{ it.isBoundToAccount }
            .observeOn(mainThreadScheduler)
            .doOnSubscribe { incrementActiveOperationsCount() }
            .doOnTerminate(::decrementActiveOperationsCount)
            .subscribeBy(
                onSuccess = { isCardBoundToAccount ->
                    nextDestinationEvent.value = if(isCardBoundToAccount){
                        IMyOrdersViewModel.NextDestination.OrderEditing(orderId)
                    } else{
                        IMyOrdersViewModel.NextDestination.NeedPaymentInformation
                    }
                },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override fun intentToNavigateToOrderInformation(orderId: Long) {
        val orderStatus = orders.value?.first{ it.id == orderId }?.status ?: return
        if(orderStatus == Order.Status.JUST_MADE) return

        nextDestinationEvent.value = IMyOrdersViewModel.NextDestination.OrderInformation(orderId)
    }

    override fun intentToCloseCurrentDestination() {
        nextDestinationEvent.value = IMyOrdersViewModel.NextDestination.Close
    }

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }

    init{
        closeWhenNotAuthorized()
        prepareData()
    }

    private fun prepareData(){
        dataPreparationDisposable?.dispose()

        dataPreparationDisposable = Completable
            .mergeArray(
                prepareOrders()
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

    private fun prepareOrders(): Completable{
        return productsOrderRepository
            .getOrders()
            .observeOn(mainThreadScheduler)
            .flatMapCompletable { orders ->
                Completable.fromRunnable {
                    this.orders.value = orders.reversed()
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
                        nextDestinationEvent.value = IMyOrdersViewModel.NextDestination.Close
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