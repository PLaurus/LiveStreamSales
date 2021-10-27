package tv.wfc.livestreamsales.features.mainpage.di.modules.diffutils

import androidx.recyclerview.widget.DiffUtil
import dagger.Module
import dagger.Provides
import tv.wfc.livestreamsales.application.model.stream.PublicStream

@Module
class DiffUtilsModule {
    @Provides
    internal fun provideBroadcastsDiffUtilCallback(): DiffUtil.ItemCallback<PublicStream>{
        return object: DiffUtil.ItemCallback<PublicStream>(){
            override fun areItemsTheSame(
                oldItem: PublicStream,
                newItem: PublicStream
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: PublicStream,
                newItem: PublicStream
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}