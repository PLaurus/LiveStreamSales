package tv.wfc.livestreamsales.features.myorders.di.modules.diffutils

import androidx.recyclerview.widget.DiffUtil
import dagger.Module
import dagger.Provides
import tv.wfc.livestreamsales.application.model.orders.Order
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

    @Provides
    internal fun provideOrdersDiffUtilItemCallback(): DiffUtil.ItemCallback<Order>{
        return object: DiffUtil.ItemCallback<Order>(){
            override fun areItemsTheSame(
                oldItem: Order,
                newItem: Order
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Order,
                newItem: Order
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}