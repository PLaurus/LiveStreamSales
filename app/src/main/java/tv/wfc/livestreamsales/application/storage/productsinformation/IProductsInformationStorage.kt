package tv.wfc.livestreamsales.application.storage.productsinformation

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import tv.wfc.livestreamsales.application.model.productinformation.ProductInformation

interface IProductsInformationStorage {
    fun getProducts(broadcastId: Long): Single<List<ProductInformation>>
    fun saveProducts(broadcastId: Long, products: List<ProductInformation>): Completable
}