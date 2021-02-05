package tv.wfc.livestreamsales.features.greeting.di.modules.viewmodel

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import tv.wfc.livestreamsales.application.di.modules.viewmodelprovider.mapkeys.ViewModelKey
import tv.wfc.livestreamsales.features.greeting.di.scope.GreetingFeatureScope
import tv.wfc.livestreamsales.features.greeting.viewmodel.GreetingViewModel
import tv.wfc.livestreamsales.features.greeting.viewmodel.IGreetingViewModel

@Module
abstract class GreetingViewModelModule {
    companion object{
        @GreetingFeatureScope
        @Provides
        @JvmStatic
        fun provideIGreetingViewModel(
            activity: AppCompatActivity,
            viewModelProviderFactory: ViewModelProvider.Factory
        ): IGreetingViewModel {
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