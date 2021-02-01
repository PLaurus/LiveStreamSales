package tv.wfc.livestreamsales.di.components.app.subscomponents.greeting.modules.listadapters

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import tv.wfc.livestreamsales.model.application.greetingpage.GreetingPage
import tv.wfc.livestreamsales.ui.activity.greeting.adapters.greetingpage.GreetingPageViewHolder
import tv.wfc.livestreamsales.ui.activity.greeting.adapters.greetingpage.GreetingPagesAdapter
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