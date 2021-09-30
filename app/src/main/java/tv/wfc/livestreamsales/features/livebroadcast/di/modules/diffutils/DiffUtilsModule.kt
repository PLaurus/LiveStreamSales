package tv.wfc.livestreamsales.features.livebroadcast.di.modules.diffutils

import androidx.recyclerview.widget.DiffUtil
import dagger.Module
import dagger.Provides
import tv.wfc.livestreamsales.application.model.streamchatmessage.StreamChatMessage
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
    internal fun provideMessagesDiffUtilCallback(): DiffUtil.ItemCallback<StreamChatMessage>{
        return object: DiffUtil.ItemCallback<StreamChatMessage>(){
            override fun areItemsTheSame(
                oldItem: StreamChatMessage,
                newItem: StreamChatMessage
            ): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: StreamChatMessage,
                newItem: StreamChatMessage
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}