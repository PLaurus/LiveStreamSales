package tv.wfc.livestreamsales.features.greeting.di.modules.listadapters

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import tv.wfc.livestreamsales.features.greeting.model.GreetingPage
import tv.wfc.livestreamsales.features.greeting.ui.adapters.greetingpage.GreetingPageViewHolder
import tv.wfc.livestreamsales.features.greeting.ui.adapters.greetingpage.GreetingPagesAdapter
import dagger.Module
import dagger.Provides

@Module
class ListAdaptersModule {
    @Provides
    internal fun provideGreetingPagesListAdapter(
        greetingPagesDiffUtilCallback: DiffUtil.ItemCallback<GreetingPage>
    ): ListAdapter<GreetingPage, GreetingPageViewHolder>{
        return GreetingPagesAdapter(greetingPagesDiffUtilCallback)
    }
}