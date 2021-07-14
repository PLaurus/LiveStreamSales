package tv.wfc.livestreamsales.application.repository.products

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import tv.wfc.livestreamsales.application.di.modules.storage.qualifiers.ProductsLocalStorage
import tv.wfc.livestreamsales.application.di.modules.storage.qualifiers.ProductsRemoteStorage
import tv.wfc.livestreamsales.application.model.products.ProductGroup
import tv.wfc.livestreamsales.application.storage.products.IProductsStorage
import javax.inject.Inject

class ProductsRepository @Inject constructor(
    @ProductsRemoteStorage
    private val productsInformationRemoteStorage: IProductsStorage,
    @ProductsLocalStorage
    private val productsInformationLocalStorage: IProductsStorage,
    private val applicationErrorsLogger: IApplicationErrorsLogger
): IProductsRepository {
    private val disposables = CompositeDisposable()

    override fun getProductGroups(broadcastId: Long): Observable<List<ProductGroup>> {
        return productsInformationLocalStorage
            .getProductGroups(broadcastId)
            .onErrorComplete().toObservable()
            .concatWith(getAndSaveProductsFromRemote(broadcastId).toObservable())
    }

    private fun getAndSaveProductsFromRemote(broadcastId: Long): Single<List<ProductGroup>> {
        return productsInformationRemoteStorage
            .getProductGroups(broadcastId)
            .doOnSuccess { saveProductsLocally(broadcastId, it) }
    }

    private fun saveProductsLocally(
        broadcastId: Long,
        productGroups: List<ProductGroup>
    ){
        productsInformationLocalStorage
            .saveProducts(broadcastId, productGroups)
            .subscribeBy(
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }
}