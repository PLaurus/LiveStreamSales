package tv.wfc.livestreamsales.application.repository.products

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import tv.wfc.livestreamsales.application.di.modules.datasource.qualifiers.ProductsLocalDataStore
import tv.wfc.livestreamsales.application.di.modules.datasource.qualifiers.ProductsRemoteDataStore
import tv.wfc.livestreamsales.application.model.products.ProductGroup
import tv.wfc.livestreamsales.application.storage.products.IProductsDataStore
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import javax.inject.Inject

class ProductsRepository @Inject constructor(
    @ProductsRemoteDataStore
    private val productsRemoteDataStore: IProductsDataStore,
    @ProductsLocalDataStore
    private val productsLocalDataStore: IProductsDataStore,
    private val applicationErrorsLogger: IApplicationErrorsLogger
): IProductsRepository {
    private val disposables = CompositeDisposable()

    override fun getProductGroups(broadcastId: Long): Observable<List<ProductGroup>> {
        return productsLocalDataStore
            .getProductGroups(broadcastId)
            .onErrorComplete().toObservable()
            .concatWith(getAndSaveProductsFromRemote(broadcastId).toObservable())
    }

    private fun getAndSaveProductsFromRemote(broadcastId: Long): Single<List<ProductGroup>> {
        return productsRemoteDataStore
            .getProductGroups(broadcastId)
            .doOnSuccess { saveProductsLocally(broadcastId, it) }
    }

    private fun saveProductsLocally(
        broadcastId: Long,
        productGroups: List<ProductGroup>
    ){
        productsLocalDataStore
            .saveProducts(broadcastId, productGroups)
            .subscribeBy(
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }
}