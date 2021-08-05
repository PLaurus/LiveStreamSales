package tv.wfc.livestreamsales.features.livebroadcast.di.modules.diffutils

import androidx.recyclerview.widget.DiffUtil
import dagger.Module
import dagger.Provides
import tv.wfc.livestreamsales.application.model.chat.ChatMessage
import tv.wfc.livestreamsales.application.model.products.ProductGroup

@Module
class DiffUtilsModule {
    @Provides
    internal fun provideProductsDiffUtilCallback(): DiffUtil.ItemCallback<ProductGroup>{
        return object: DiffUtil.ItemCallback<ProductGroup>(){
            override fun areItemsTheSame(
                oldItem: ProductGroup,
                newItem: ProductGroup
            ): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(
                oldItem: ProductGroup,
                newItem: ProductGroup
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    @Provides
    internal fun provideMessagesDiffUtilCallback(): DiffUtil.ItemCallback<ChatMessage>{
        return object: DiffUtil.ItemCallback<ChatMessage>(){
            override fun areItemsTheSame(
                oldItem: ChatMessage,
                newItem: ChatMessage
            ): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: ChatMessage,
                newItem: ChatMessage
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}