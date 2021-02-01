package tv.wfc.livestreamsales.di.components.app.modules.viewmodelprovider

import androidx.lifecycle.ViewModelProvider
import tv.wfc.livestreamsales.viewmodels.ViewModelProviderFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelProviderModule {
    @Binds
    internal abstract fun provideViewModelProviderFactory(
        viewModelProviderFactory: ViewModelProviderFactory
    ): ViewModelProvider.Factory
}