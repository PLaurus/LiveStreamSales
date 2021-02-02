package tv.wfc.livestreamsales.di.components.app.subscomponents.greeting.modules.viewmodel

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tv.wfc.livestreamsales.di.scopes.ActivityScope
import tv.wfc.livestreamsales.viewmodels.ViewModelProviderFactory
import tv.wfc.livestreamsales.viewmodels.greeting.GreetingViewModel
import tv.wfc.livestreamsales.viewmodels.greeting.IGreetingViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import tv.wfc.livestreamsales.di.mapkeys.ViewModelKey

@Module
abstract class GreetingViewModelModule {
    companion object{
        @ActivityScope
        @Provides
        @JvmStatic
        fun provideIGreetingViewModel(
            activity: AppCompatActivity,
            viewModelProviderFactory: ViewModelProvider.Factory
        ): IGreetingViewModel{
            return ViewModelProvider(
                activity,
                viewModelProviderFactory
            )[GreetingViewModel::class.java]
        }
    }

    @Binds
    @IntoMap
    @ViewModelKey(GreetingViewModel::class)
    abstract fun bindGreetingViewModelIntoMap(
        greetingViewModel: GreetingViewModel
    ): ViewModel
}