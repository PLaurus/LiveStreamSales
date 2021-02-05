package tv.wfc.livestreamsales.features.mainpage.di.modules.diffutils

import androidx.recyclerview.widget.DiffUtil
import dagger.Module
import dagger.Provides
import tv.wfc.livestreamsales.application.model.broadcastinformation.BroadcastBaseInformation

@Module
class DiffUtilsModule {
    @Provides
    internal fun provideBroadcastsDiffUtilCallback(): DiffUtil.ItemCallback<BroadcastBaseInformation>{
        return object: DiffUtil.ItemCallback<BroadcastBaseInformation>(){
            override fun areItemsTheSame(
                oldItem: BroadcastBaseInformation,
                newItem: BroadcastBaseInformation
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: BroadcastBaseInformation,
                newItem: BroadcastBaseInformation
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}