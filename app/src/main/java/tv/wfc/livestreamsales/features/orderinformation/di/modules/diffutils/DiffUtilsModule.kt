package tv.wfc.livestreamsales.features.orderinformation.di.modules.diffutils

import androidx.recyclerview.widget.DiffUtil
import dagger.Module
import dagger.Provides
import tv.wfc.livestreamsales.application.model.orders.OrderedProduct

@Module
class DiffUtilsModule {
    @Provides
    internal fun provideOrderedProductsDiffUtilItemCallback(): DiffUtil.ItemCallback<OrderedProduct>{
        return object: DiffUtil.ItemCallback<OrderedProduct>(){
            override fun areItemsTheSame(
                oldItem: OrderedProduct,
                newItem: OrderedProduct
            ): Boolean {
                return oldItem.product.id == newItem.product.id
            }

            override fun areContentsTheSame(
                oldItem: OrderedProduct,
                newItem: OrderedProduct
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}