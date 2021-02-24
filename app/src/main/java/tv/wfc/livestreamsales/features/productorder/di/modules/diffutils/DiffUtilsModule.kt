package tv.wfc.livestreamsales.features.productorder.di.modules.diffutils

import androidx.recyclerview.widget.DiffUtil
import dagger.Module
import dagger.Provides
import tv.wfc.livestreamsales.features.productorder.di.modules.diffutils.qualifiers.ProductSpecificationsDiffUtilItemCallback

@Module
class DiffUtilsModule {
    @Provides
    @ProductSpecificationsDiffUtilItemCallback
    fun provideProductSpecificationsDiffUtilItemCallback(): DiffUtil.ItemCallback<Pair<String, String?>>{
        return object: DiffUtil.ItemCallback<Pair<String, String?>>(){
            override fun areItemsTheSame(
                oldItem: Pair<String, String?>,
                newItem: Pair<String, String?>
            ): Boolean {
                return oldItem.first == newItem.first
            }

            override fun areContentsTheSame(
                oldItem: Pair<String, String?>,
                newItem: Pair<String, String?>
            ): Boolean {
                return oldItem.first == newItem.first && oldItem.second == newItem.second
            }
        }
    }
}