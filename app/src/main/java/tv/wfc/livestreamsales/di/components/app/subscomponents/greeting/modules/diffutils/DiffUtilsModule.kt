package tv.wfc.livestreamsales.di.components.app.subscomponents.greeting.modules.diffutils

import androidx.recyclerview.widget.DiffUtil
import tv.wfc.livestreamsales.model.application.greetingpage.GreetingPage
import dagger.Module
import dagger.Provides

@Module
class DiffUtilsModule {
    @Provides
    internal fun provideGreetingPagesDiffUtilCallback(): DiffUtil.ItemCallback<GreetingPage>{
        return object: DiffUtil.ItemCallback<GreetingPage>(){
            override fun areItemsTheSame(oldItem: GreetingPage, newItem: GreetingPage): Boolean {
                return oldItem.pageOrder == newItem.pageOrder
            }

            override fun areContentsTheSame(oldItem: GreetingPage, newItem: GreetingPage): Boolean {
                return oldItem == newItem
            }
        }
    }
}