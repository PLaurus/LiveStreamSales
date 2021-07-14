package tv.wfc.livestreamsales.application.storage.products.local

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.application.model.products.ProductGroup
import tv.wfc.livestreamsales.application.storage.products.IProductsStorage
import javax.inject.Inject

class ProductsLocalStorage @Inject constructor(
    @IoScheduler
    private val ioScheduler: Scheduler
): IProductsStorage {
    private val products = mutableMapOf<Long, List<ProductGroup>>()

    override fun getProductGroups(broadcastId: Long): Single<List<ProductGroup>> {
        return Single
            .fromCallable { products.getValue(broadcastId) }
            .subscribeOn(ioScheduler)
    }

    override fun saveProducts(broadcastId: Long, productGroups: List<ProductGroup>): Completable {
        return Completable
            .fromRunnable { this.products[broadcastId] = productGroups }
            .subscribeOn(ioScheduler)
    }
}