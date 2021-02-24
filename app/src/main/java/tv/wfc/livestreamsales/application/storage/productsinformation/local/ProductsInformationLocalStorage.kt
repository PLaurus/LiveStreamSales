package tv.wfc.livestreamsales.application.storage.productsinformation.local

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.application.model.Product
import tv.wfc.livestreamsales.application.storage.productsinformation.IProductsInformationStorage
import javax.inject.Inject

class ProductsInformationLocalStorage @Inject constructor(
    @IoScheduler
    private val ioScheduler: Scheduler
): IProductsInformationStorage {
    override fun getProducts(broadcastId: Long): Single<List<Product>> {
        return Single
            .fromCallable { products.getValue(broadcastId) }
            .subscribeOn(ioScheduler)
    }

    override fun saveProducts(broadcastId: Long, products: List<Product>): Completable {
        return Completable
            .fromRunnable { this.products[broadcastId] = products }
            .subscribeOn(ioScheduler)
    }

    private val products = mutableMapOf<Long, List<Product>>()
}