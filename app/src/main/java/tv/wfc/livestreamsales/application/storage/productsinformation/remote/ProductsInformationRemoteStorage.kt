package tv.wfc.livestreamsales.application.storage.productsinformation.remote

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import tv.wfc.livestreamsales.BuildConfig
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.application.model.Product
import tv.wfc.livestreamsales.application.storage.productsinformation.IProductsInformationStorage
import tv.wfc.livestreamsales.features.rest.api.notauthorized.IProductsApi
import javax.inject.Inject

class ProductsInformationRemoteStorage @Inject constructor(
    private val productsApi: IProductsApi,
    @IoScheduler
    private val ioScheduler: Scheduler
): IProductsInformationStorage {
    override fun getProducts(broadcastId: Long): Single<List<Product>> {
        if(BuildConfig.DEBUG && BuildConfig.IgnoreRestRequests){
            return Single
                .just(listOf(Product(
                    id = 0,
                    name = "IPhone 12",
                    price = 120234.4f,
                    oldPrice = 180000.0f,
                    imageUrl = "https://www.searchpng.com/wp-content/uploads/2018/12/iphone-x-mockup-1024x1024.png",
                    availableAmount = 29,
                    availableColors = mapOf(0L to "#FFFFFF", 1L to "#FF8844", 2L to "#FFBBAA"),
                    characteristics = mapOf(
                        "Длинна" to "1 км",
                        "Пахнет рыбой" to "Да",
                        "Экран разбит в дребезги" to "Естессна",
                        "Длинный текст чисто для галочки ивановны" to "Магомед"
                    )
                )))
                .subscribeOn(ioScheduler)
        }

        return productsApi
            .getProducts(broadcastId)
            .map { it.data }
            .subscribeOn(ioScheduler)
    }

    override fun saveProducts(broadcastId: Long, products: List<Product>): Completable {
        return Completable
            .fromRunnable {
                throw NotImplementedError()
            }
            .subscribeOn(ioScheduler)
    }
}