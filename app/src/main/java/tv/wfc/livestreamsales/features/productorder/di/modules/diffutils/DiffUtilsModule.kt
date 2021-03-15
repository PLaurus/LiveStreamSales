package tv.wfc.livestreamsales.features.productorder.di.modules.diffutils

import androidx.recyclerview.widget.DiffUtil
import dagger.Module
import dagger.Provides
import tv.wfc.livestreamsales.application.model.products.specification.Specification
import tv.wfc.livestreamsales.features.productorder.di.modules.diffutils.qualifiers.ProductBoxDataDiffUtilItemCallback
import tv.wfc.livestreamsales.features.productorder.di.modules.diffutils.qualifiers.ProductSpecificationsDiffUtilItemCallback
import tv.wfc.livestreamsales.features.productorder.model.ProductBoxData
import tv.wfc.livestreamsales.features.productorder.model.ProductInCart
import tv.wfc.livestreamsales.features.productorder.model.SelectableSpecification

@Module
class DiffUtilsModule {
    @Provides
    @ProductBoxDataDiffUtilItemCallback
    fun provideProductBoxDataDiffUtilItemCallback(): DiffUtil.ItemCallback<ProductBoxData>{
        return object: DiffUtil.ItemCallback<ProductBoxData>(){
            override fun areItemsTheSame(
                oldItem: ProductBoxData,
                newItem: ProductBoxData
            ): Boolean {
                return oldItem.position == newItem.position
            }

            override fun areContentsTheSame(
                oldItem: ProductBoxData,
                newItem: ProductBoxData
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    @Provides
    @ProductSpecificationsDiffUtilItemCallback
    fun provideProductSpecificationsDiffUtilItemCallback(): DiffUtil.ItemCallback<Specification<*>>{
        return object: DiffUtil.ItemCallback<Specification<*>>(){
            override fun areItemsTheSame(
                oldItem: Specification<*>,
                newItem: Specification<*>
            ): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(
                oldItem: Specification<*>,
                newItem: Specification<*>
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    @Provides
    fun provideSelectableSpecificationsDiffUtilItemCallback(): DiffUtil.ItemCallback<SelectableSpecification<*>>{
        return object: DiffUtil.ItemCallback<SelectableSpecification<*>>(){
            override fun areItemsTheSame(
                oldItem: SelectableSpecification<*>,
                newItem: SelectableSpecification<*>
            ): Boolean {
                return oldItem.name == newItem.name && oldItem::class == newItem::class
            }

            override fun areContentsTheSame(
                oldItem: SelectableSpecification<*>,
                newItem: SelectableSpecification<*>
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    @Provides
    fun provideProductInCartDiffUtilItemCallback(): DiffUtil.ItemCallback<ProductInCart>{
        return object: DiffUtil.ItemCallback<ProductInCart>(){
            override fun areItemsTheSame(
                oldItem: ProductInCart,
                newItem: ProductInCart
            ): Boolean {
                return oldItem.product == newItem.product
            }

            override fun areContentsTheSame(
                oldItem: ProductInCart,
                newItem: ProductInCart
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}