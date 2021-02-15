package tv.wfc.livestreamsales.application.repository.productsinformation

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import tv.wfc.livestreamsales.application.di.modules.storage.qualifiers.ProductsInformationLocalStorage
import tv.wfc.livestreamsales.application.di.modules.storage.qualifiers.ProductsInformationRemoteStorage
import tv.wfc.livestreamsales.application.model.productinformation.ProductInformation
import tv.wfc.livestreamsales.application.storage.productsinformation.IProductsInformationStorage
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import javax.inject.Inject

class ProductsInformationRepository @Inject constructor(
    @ProductsInformationRemoteStorage
    private val productsInformationRemoteStorage: IProductsInformationStorage,
    @ProductsInformationLocalStorage
    private val productsInformationLocalStorage: IProductsInformationStorage,
    private val applicationErrorsLogger: IApplicationErrorsLogger
): IProductsInformationRepository {
    override fun getProducts(broadcastId: Long): Observable<List<ProductInformation>> {
        return productsInformationLocalStorage
            .getProducts(broadcastId)
            .onErrorComplete().toObservable()
            .concatWith(getAndSaveProductsFromRemote(broadcastId).toObservable())
    }

    private val disposables = CompositeDisposable()

    private fun getAndSaveProductsFromRemote(broadcastId: Long): Single<List<ProductInformation>>{
        return productsInformationRemoteStorage
            .getProducts(broadcastId)
            .doOnSuccess { saveProductsLocally(broadcastId, it) }
    }

    private fun saveProductsLocally(
        broadcastId: Long,
        products: List<ProductInformation>
    ){
        productsInformationLocalStorage
            .saveProducts(broadcastId, products)
            .subscribeBy(
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }
}