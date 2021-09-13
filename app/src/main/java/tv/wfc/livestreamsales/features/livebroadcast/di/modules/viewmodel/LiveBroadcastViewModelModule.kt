package tv.wfc.livestreamsales.features.livebroadcast.di.modules.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import tv.wfc.livestreamsales.application.di.modules.viewmodelprovider.mapkeys.ViewModelKey
import tv.wfc.livestreamsales.features.livebroadcast.di.qualifiers.LiveBroadcastFragment
import tv.wfc.livestreamsales.features.livebroadcast.di.scope.LiveBroadcastFeatureScope
import tv.wfc.livestreamsales.features.livebroadcast.viewmodel.ILiveBroadcastViewModel
import tv.wfc.livestreamsales.features.livebroadcast.viewmodel.LiveBroadcastViewModel

@Module
abstract class LiveBroadcastViewModelModule {
    companion object{
        @LiveBroadcastFeatureScope
        @Provides
        internal fun provideILiveBroadcastViewModel(
            @LiveBroadcastFragment
            fragment: Fragment,
            viewModelProviderFactory: ViewModelProvider.Factory
        ): ILiveBroadcastViewModel {
            return ViewModelProvider(
                fragment,
                viewModelProviderFactory
            )[LiveBroadcastViewModel::class.java]
        }
    }

    @Binds
    @IntoMap
    @ViewModelKey(LiveBroadcastViewModel::class)
    internal abstract fun bindLiveBroadcastViewModelIntoMap(
        liveBroadcastViewModel: LiveBroadcastViewModel
    ): ViewModel
}