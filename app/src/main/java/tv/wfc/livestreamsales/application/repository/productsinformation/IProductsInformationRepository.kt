package tv.wfc.livestreamsales.application.repository.productsinformation

import io.reactivex.rxjava3.core.Observable
import tv.wfc.livestreamsales.application.model.productinformation.ProductInformation

interface IProductsInformationRepository {
    fun getProducts(broadcastId: Long): Observable<List<ProductInformation>>
}