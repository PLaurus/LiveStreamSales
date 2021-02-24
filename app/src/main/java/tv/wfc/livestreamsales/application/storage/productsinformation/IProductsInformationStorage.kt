package tv.wfc.livestreamsales.application.storage.productsinformation

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import tv.wfc.livestreamsales.application.model.Product

interface IProductsInformationStorage {
    fun getProducts(broadcastId: Long): Single<List<Product>>
    fun saveProducts(broadcastId: Long, products: List<Product>): Completable
}