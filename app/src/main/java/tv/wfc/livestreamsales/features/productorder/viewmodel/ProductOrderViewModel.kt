package tv.wfc.livestreamsales.features.productorder.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.PublishSubject
import tv.wfc.contentloader.model.ViewModelPreparationState
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.application.model.Product
import tv.wfc.livestreamsales.application.repository.productsinformation.IProductsInformationRepository
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import javax.inject.Inject

class ProductOrderViewModel @Inject constructor(
    private val productsRepository: IProductsInformationRepository,
    @MainThreadScheduler
    private val mainThreadScheduler: Scheduler,
    private val applicationErrorsLogger: IApplicationErrorsLogger
): ViewModel(), IProductOrderViewModel {
    private val disposables = CompositeDisposable()
    private val productsSubject = PublishSubject.create<List<Product>>()
    override val dataPreparationState = MutableLiveData<ViewModelPreparationState>()

    override val products: LiveData<List<Product>> = MutableLiveData<List<Product>>().apply {
        productsSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = ::setValue,
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val selectedProduct = MutableLiveData<Product>().apply {
        productsSubject
            .filter { it.isNotEmpty() }
            .map { it[0] }
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = ::setValue,
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val productsCount: LiveData<Int> = MutableLiveData<Int>().apply {
        productsSubject
            .map{ it.size }
            .distinctUntilChanged()
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = ::setValue,
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override fun prepareData(broadcastId: Long){
        if(dataPreparationState.value != ViewModelPreparationState.DataIsNotPrepared &&
            dataPreparationState.value != null) return

        Completable
            .merge(listOf(
                prepareProducts(broadcastId)
            ))
            .observeOn(mainThreadScheduler)
            .doOnSubscribe { dataPreparationState.value = ViewModelPreparationState.DataIsBeingPrepared }
            .doOnError(applicationErrorsLogger::logError)
            .subscribeBy(
                onComplete = {
                    dataPreparationState.value = ViewModelPreparationState.DataIsPrepared
                },
                onError = {
                    dataPreparationState.value = ViewModelPreparationState.FailedToPrepareData()
                }
            )
            .addTo(disposables)

    }

    override fun selectProductByPosition(position: Int) {
        products.value?.getOrNull(position)?.let { selectedProduct ->
            this.selectedProduct.value = selectedProduct
        }
    }

    private fun prepareProducts(broadcastId: Long): Completable{
        return  Completable.create{ emitter ->
            val disposable = productsRepository
                .getProducts(broadcastId)
                .lastOrError()
                .filter{ it.isNotEmpty() }
                .toSingle()
                .observeOn(mainThreadScheduler)
                .subscribeBy(
                    onSuccess = {
                        productsSubject.onNext(it)
                        emitter.onComplete()
                    },
                    onError = emitter::onError
                )

            emitter.setDisposable(disposable)
        }
    }
}