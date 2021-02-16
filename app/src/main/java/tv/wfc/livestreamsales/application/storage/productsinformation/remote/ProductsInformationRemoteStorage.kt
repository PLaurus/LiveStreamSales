package tv.wfc.livestreamsales.application.storage.productsinformation.remote

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import tv.wfc.livestreamsales.BuildConfig
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
        if(BuildConfig.DEBUG && BuildConfig.IgnoreRestRequests){
            return Single
                .just(listOf(ProductInformation(
                    id = 0,
                    name = "IPhone 12",
                    price = 120234.4f,
                    oldPrice = 180000.0f,
                    imageUrl = "https://wiki-smart.ru/wp-content/uploads/2020/11/iphone-12-v-podarok-esli-kupit-huawei-mate-40-pro-povezlo-tolko-kitajczam.jpg",
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