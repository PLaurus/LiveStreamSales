package tv.wfc.livestreamsales.application.storage.products.remote

import android.content.Context
import android.graphics.Color
import androidx.core.graphics.toColorInt
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import tv.wfc.livestreamsales.BuildConfig
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.application.model.exception.storage.NoSuchDataInStorageException
import tv.wfc.livestreamsales.application.model.exception.storage.ReceivedDataWithWrongFormatException
import tv.wfc.livestreamsales.application.model.products.ProductGroup
import tv.wfc.livestreamsales.application.model.products.ProductVariant
import tv.wfc.livestreamsales.application.model.products.specification.Specification
import tv.wfc.livestreamsales.application.storage.products.IProductsStorage
import tv.wfc.livestreamsales.features.rest.api.notauthorized.IBroadcastsApi
import tv.wfc.livestreamsales.features.rest.model.broadcasts.Stream
import tv.wfc.livestreamsales.features.rest.model.products.Property
import tv.wfc.livestreamsales.features.rest.model.products.Sku
import javax.inject.Inject

private typealias RemoteProduct = tv.wfc.livestreamsales.features.rest.model.products.Product

class ProductsRemoteStorage @Inject constructor(
    private val context: Context,
    private val broadcastsApi: IBroadcastsApi,
    @IoScheduler
    private val ioScheduler: Scheduler
): IProductsStorage {
    override fun getProducts(broadcastId: Long): Single<List<ProductGroup>> {
        if(BuildConfig.DEBUG && BuildConfig.IgnoreRestRequests){
            return Single
                .just(createDebugProducts())
                .subscribeOn(ioScheduler)
        }

        return broadcastsApi
            .getBroadcast(broadcastId)
            .map{ it.data ?: throw NoSuchDataInStorageException() }
            .map { getProductsFromStream(it) ?: throw ReceivedDataWithWrongFormatException() }
            .subscribeOn(ioScheduler)
    }

    override fun saveProducts(broadcastId: Long, productGroups: List<ProductGroup>): Completable {
        return Completable
            .fromRunnable {
                throw NotImplementedError()
            }
            .subscribeOn(ioScheduler)
    }

    private fun getProductsFromStream(stream: Stream): List<ProductGroup>?{
        val streamProducts = stream.products?.filterNotNull() ?: return null
        return streamProducts.mapNotNull { it.toProduct() }
    }

    private fun RemoteProduct.toProduct(): ProductGroup?{
        val productName = name ?: return null
        val productVariants = skus?.mapNotNull { it.toProductVariant() } ?: return null
        val productImage = null
        val productDescription = description
        val productSpecifications = emptyList<Specification<*>>()

        return ProductGroup(
            name = productName,
            productVariants = productVariants,
            image = productImage,
            description = productDescription,
            specifications = productSpecifications
        )
    }

    private fun Sku.toProductVariant(): ProductVariant?{
        val productVariantId = 0L
        val productVariantQuantityInStock = inStock ?: return null
        val productVariantPrice = price ?: return null
        val specifications = properties?.mapNotNull{ it.toSpecification() } ?: emptyList()

        return ProductVariant(
            id = productVariantId,
            quantityInStock = productVariantQuantityInStock,
            price = productVariantPrice,
            specifications = specifications
        )
    }

    private fun Property.toSpecification(): Specification<*>? {
        val specificationType = type ?: return null
        val name = name ?: return null
        val value = value ?: return null

        return when(specificationType){
            "regular" -> {
                Specification.DescriptiveSpecification(name, value)
            }
            "color" -> {
                try{
                    val color = colorHex?.toColorInt() ?: return null
                    Specification.ColorSpecification(name, color)
                } catch (ex: IllegalArgumentException){
                    null
                }
            }
            else -> null
        }
    }

    private fun createDebugProducts(): List<ProductGroup>{
        val product1 = ProductGroup(
            name = "IPhone 12",
            productVariants = listOf(
                ProductVariant(
                    id = 0,
                    quantityInStock = 199,
                    price = 180_000.50f,
                    specifications = listOf(
                        Specification.ColorSpecification(
                            context.getString(R.string.product_specification_name_color),
                            Color.parseColor("#FFFFFF")
                        ),
                        Specification.DescriptiveSpecification(name = "Память", "128gb"),
                        Specification.DescriptiveSpecification(name = "Материал", "Сталь")
                    )
                ),
                ProductVariant(
                    id = 1,
                    quantityInStock = 30,
                    price = 189_000.50f,
                    specifications = listOf(
                        Specification.ColorSpecification(
                            context.getString(R.string.product_specification_name_color),
                            Color.parseColor("#FFFFFF")
                        ),
                        Specification.DescriptiveSpecification(name = "Память", "128gb"),
                        Specification.DescriptiveSpecification(name = "Материал", "Золото")
                    )
                ),
                ProductVariant(
                    id = 2,
                    quantityInStock = 3,
                    price = 300_000.50f,
                    specifications = listOf(
                        Specification.ColorSpecification(
                            context.getString(R.string.product_specification_name_color),
                            Color.parseColor("#FFAAAA")
                        ),
                        Specification.DescriptiveSpecification(name = "Материал", "Золото"),
                        Specification.DescriptiveSpecification(name = "Форма", "Ромб")
                    )
                ),
                ProductVariant(
                    id = 3,
                    quantityInStock = 300,
                    price = 315_000.50f,
                    specifications = listOf(
                        Specification.ColorSpecification(
                            context.getString(R.string.product_specification_name_color),
                            Color.parseColor("#000000")
                        ),
                        Specification.DescriptiveSpecification(name = "Память", "128gb"),
                        Specification.DescriptiveSpecification(name = "Материал", "Золото"),
                        Specification.DescriptiveSpecification(name = "Форма", "Куб")
                    )
                )
            ),
            image = "https://www.freepngimg.com/thumb/iphone_7/85301-technology-12-ios-iphone-telephony-free-clipart-hd.png",
            description = "Это описание товара",
            specifications = listOf(
                Specification.DescriptiveSpecification(name = "Можно звонить", "Да"),
                Specification.DescriptiveSpecification(name = "Многофункциональный", "Нет"),
                Specification.DescriptiveSpecification(name = "Предназначен для позёров", "Да")
            )
        )

        val product2 = ProductGroup(
            name = "IPhone Zero",
            productVariants = listOf(
                ProductVariant(
                    id = 4,
                    quantityInStock = 50,
                    price = 180_000.50f,
                    specifications = listOf(
                        Specification.ColorSpecification(
                            context.getString(R.string.product_specification_name_color),
                            Color.parseColor("#FFFFFF")
                        ),
                        Specification.DescriptiveSpecification(name = "Память", "128gb"),
                        Specification.DescriptiveSpecification(name = "Материал", "Сталь")
                    )
                ),
                ProductVariant(
                    id = 5,
                    quantityInStock = 1000,
                    price = 189_000.50f,
                    specifications = listOf(
                        Specification.ColorSpecification(
                            context.getString(R.string.product_specification_name_color),
                            Color.parseColor("#FFFFFF")
                        ),
                        Specification.DescriptiveSpecification(name = "Память", "128gb"),
                        Specification.DescriptiveSpecification(name = "Материал", "Золото")
                    )
                ),
                ProductVariant(
                    id = 6,
                    quantityInStock = 300,
                    price = 300_000.50f,
                    specifications = listOf(
                        Specification.ColorSpecification(
                            context.getString(R.string.product_specification_name_color),
                            Color.parseColor("#CCCCCC")
                        ),
                        Specification.DescriptiveSpecification(name = "Материал", "Золото"),
                        Specification.DescriptiveSpecification(name = "Форма", "Ромб")
                    )
                ),
                ProductVariant(
                    id = 7,
                    quantityInStock = 1,
                    price = 315_000.50f,
                    specifications = listOf(
                        Specification.ColorSpecification(
                            context.getString(R.string.product_specification_name_color),
                            Color.parseColor("#FFFFFF")
                        ),
                        Specification.DescriptiveSpecification(name = "Память", "128gb"),
                        Specification.DescriptiveSpecification(name = "Материал", "Золото"),
                        Specification.DescriptiveSpecification(name = "Форма", "Куб")
                    )
                ),
                ProductVariant(
                    id = 7,
                    quantityInStock = 1,
                    price = 315_000.50f,
                    specifications = listOf(
                        Specification.ColorSpecification(
                            context.getString(R.string.product_specification_name_color),
                            Color.parseColor("#FFFFFF")
                        ),
                        Specification.DescriptiveSpecification(name = "Память", "128gb"),
                        Specification.DescriptiveSpecification(name = "Материал", "Серебро"),
                        Specification.DescriptiveSpecification(name = "Форма", "Куб")
                    )
                )
            )
        )

        return listOf(product1, product2)
    }
}