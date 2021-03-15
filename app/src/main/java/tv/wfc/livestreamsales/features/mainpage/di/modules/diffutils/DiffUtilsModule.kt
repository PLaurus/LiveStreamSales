package tv.wfc.livestreamsales.features.mainpage.di.modules.diffutils

import androidx.recyclerview.widget.DiffUtil
import dagger.Module
import dagger.Provides
import tv.wfc.livestreamsales.application.model.broadcastinformation.Broadcast

@Module
class DiffUtilsModule {
    @Provides
    internal fun provideBroadcastsDiffUtilCallback(): DiffUtil.ItemCallback<Broadcast>{
        return object: DiffUtil.ItemCallback<Broadcast>(){
            override fun areItemsTheSame(
                oldItem: Broadcast,
                newItem: Broadcast
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Broadcast,
                newItem: Broadcast
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}