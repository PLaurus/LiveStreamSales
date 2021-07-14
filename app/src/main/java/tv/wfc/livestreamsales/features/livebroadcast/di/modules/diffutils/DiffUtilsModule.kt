package tv.wfc.livestreamsales.features.livebroadcast.di.modules.diffutils

import androidx.recyclerview.widget.DiffUtil
import dagger.Module
import dagger.Provides
import tv.wfc.livestreamsales.application.model.products.Product

@Module
class DiffUtilsModule {
    @Provides
    internal fun provideProductsDiffUtilCallback(): DiffUtil.ItemCallback<Product>{
        return object: DiffUtil.ItemCallback<Product>(){
            override fun areItemsTheSame(
                oldItem: Product,
                newItem: Product
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Product,
                newItem: Product
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}