package tv.wfc.livestreamsales.application.storage.products

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import tv.wfc.livestreamsales.application.model.products.ProductGroup

interface IProductsDataStore {
    fun getProductGroups(broadcastId: Long): Single<List<ProductGroup>>
    fun saveProducts(broadcastId: Long, productGroups: List<ProductGroup>): Completable
}