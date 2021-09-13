package tv.wfc.livestreamsales.features.liveBroadcastingDestination.di.modules.viewModel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import tv.wfc.livestreamsales.application.di.modules.viewmodelprovider.mapkeys.ViewModelKey
import tv.wfc.livestreamsales.features.liveBroadcastingDestination.di.qualifiers.LiveBroadcastingFragment
import tv.wfc.livestreamsales.features.liveBroadcastingDestination.di.scope.LiveBroadcastingFeatureScope
import tv.wfc.livestreamsales.features.liveBroadcastingDestination.viewmodel.ILiveBroadcastingViewModel
import tv.wfc.livestreamsales.features.liveBroadcastingDestination.viewmodel.LiveBroadcastingViewModel

@Module
abstract class ViewModelModule {
    companion object{
        @Provides
        @LiveBroadcastingFeatureScope
        internal fun provideILiveBroadcastingViewModel(
            @LiveBroadcastingFragment
            fragment: Fragment,
            viewModelProviderFactory: ViewModelProvider.Factory
        ): ILiveBroadcastingViewModel {
            return ViewModelProvider(
                fragment,
                viewModelProviderFactory
            )[LiveBroadcastingViewModel::class.java]
        }
    }

    @Binds
    @IntoMap
    @ViewModelKey(LiveBroadcastingViewModel::class)
    internal abstract fun bindLiveBroadcastingViewModelIntoMap(
        viewModel: LiveBroadcastingViewModel
    ): ViewModel
}