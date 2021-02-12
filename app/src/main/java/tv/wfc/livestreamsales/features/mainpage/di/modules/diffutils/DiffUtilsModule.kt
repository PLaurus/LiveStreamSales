package tv.wfc.livestreamsales.features.mainpage.di.modules.diffutils

import androidx.recyclerview.widget.DiffUtil
import dagger.Module
import dagger.Provides
import tv.wfc.livestreamsales.application.model.broadcastinformation.BroadcastInformation

@Module
class DiffUtilsModule {
    @Provides
    internal fun provideBroadcastsDiffUtilCallback(): DiffUtil.ItemCallback<BroadcastInformation>{
        return object: DiffUtil.ItemCallback<BroadcastInformation>(){
            override fun areItemsTheSame(
                oldItem: BroadcastInformation,
                newItem: BroadcastInformation
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: BroadcastInformation,
                newItem: BroadcastInformation
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}