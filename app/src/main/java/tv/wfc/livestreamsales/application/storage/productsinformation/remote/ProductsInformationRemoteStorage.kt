package tv.wfc.livestreamsales.application.storage.productsinformation.remote

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.application.model.productinformation.ProductInformation
import tv.wfc.livestreamsales.application.storage.productsinformation.IProductsInformationStorage
import tv.wfc.livestreamsales.features.rest.api.notauthorized.IProductsInformationApi
import javax.inject.Inject

class ProductsInformationRemoteStorage @Inject constructor(
    private val productsInformationApi: IProductsInformationApi,
    @IoScheduler
    private val ioScheduler: Scheduler
): IProductsInformationStorage {
    override fun getProducts(broadcastId: Long): Single<List<ProductInformation>> {
        return productsInformationApi
            .getProducts(broadcastId)
            .map { it.data }
            .subscribeOn(ioScheduler)
    }

    override fun saveProducts(broadcastId: Long, products: List<ProductInformation>): Completable {
        return Completable
            .fromRunnable {
                throw NotImplementedError()
            }
            .subscribeOn(ioScheduler)
    }
}