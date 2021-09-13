package tv.wfc.livestreamsales.features.liveBroadcastingSettingsDestination.di.modules

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import tv.wfc.livestreamsales.application.di.modules.viewmodelprovider.mapkeys.ViewModelKey
import tv.wfc.livestreamsales.features.liveBroadcastingSettingsDestination.di.qualifiers.LiveBroadcastingSettingsFragment
import tv.wfc.livestreamsales.features.liveBroadcastingSettingsDestination.di.scope.LiveBroadcastingSettingsFeatureScope
import tv.wfc.livestreamsales.features.liveBroadcastingSettingsDestination.viewmodel.ILiveBroadcastingSettingsViewModel
import tv.wfc.livestreamsales.features.liveBroadcastingSettingsDestination.viewmodel.LiveBroadcastingSettingsViewModel

@Module
abstract class ViewModelModule {
    companion object{
        @LiveBroadcastingSettingsFeatureScope
        @Provides
        internal fun provideILiveBroadcastingSettingsViewModel(
            @LiveBroadcastingSettingsFragment
            fragment: Fragment,
            viewModelProviderFactory: ViewModelProvider.Factory
        ): ILiveBroadcastingSettingsViewModel{
            return ViewModelProvider(
                fragment,
                viewModelProviderFactory
            )[LiveBroadcastingSettingsViewModel::class.java]
        }
    }

    @Binds
    @IntoMap
    @ViewModelKey(LiveBroadcastingSettingsViewModel::class)
    internal abstract fun bindLiveBroadcastingSettingsViewModelIntoMap(
        viewModel: LiveBroadcastingSettingsViewModel
    ): ViewModel
}