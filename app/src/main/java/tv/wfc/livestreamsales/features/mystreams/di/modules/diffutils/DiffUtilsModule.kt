package tv.wfc.livestreamsales.features.mystreams.di.modules.diffutils

import androidx.recyclerview.widget.DiffUtil
import dagger.Module
import dagger.Provides
import tv.wfc.livestreamsales.application.model.stream.MyStream

@Module
class DiffUtilsModule {
    @Provides
    internal fun provideMyStreamsDiffUtilItemCallback() =
        object : DiffUtil.ItemCallback<MyStream>() {
            override fun areItemsTheSame(oldItem: MyStream, newItem: MyStream): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MyStream, newItem: MyStream): Boolean {
                return oldItem == newItem
            }
        }
}