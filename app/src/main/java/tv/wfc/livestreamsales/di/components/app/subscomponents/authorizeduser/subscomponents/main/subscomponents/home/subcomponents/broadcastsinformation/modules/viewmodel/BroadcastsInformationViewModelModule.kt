package tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.subscomponents.main.subscomponents.home.subcomponents.broadcastsinformation.modules.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.subscomponents.main.subscomponents.home.subcomponents.broadcastsinformation.qualifiers.BroadcastsInformationFragment
import tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.subscomponents.main.subscomponents.home.subcomponents.broadcastsinformation.qualifiers.BroadcastsInformationFragmentScope
import tv.wfc.livestreamsales.di.mapkeys.ViewModelKey
import tv.wfc.livestreamsales.viewmodels.broadcastsinformation.BroadcastsInformationViewModel
import tv.wfc.livestreamsales.viewmodels.broadcastsinformation.IBroadcastsInformationViewModel

@Module
abstract class BroadcastsInformationViewModelModule {
    companion object{
        @BroadcastsInformationFragmentScope
        @Provides
        @JvmStatic
        internal fun provideIBroadcastsInformationViewModel(
            @BroadcastsInformationFragment
            fragment: Fragment,
            viewModelProviderFactory: ViewModelProvider.Factory
        ): IBroadcastsInformationViewModel{
            return ViewModelProvider(
                fragment,
                viewModelProviderFactory
            )[BroadcastsInformationViewModel::class.java]
        }
    }

    @Binds
    @IntoMap
    @ViewModelKey(BroadcastsInformationViewModel::class)
    internal abstract fun bindBroadcastsInformationViewModelIntoMap(
        broadcastsInformationViewModel: BroadcastsInformationViewModel
    ): ViewModel
}