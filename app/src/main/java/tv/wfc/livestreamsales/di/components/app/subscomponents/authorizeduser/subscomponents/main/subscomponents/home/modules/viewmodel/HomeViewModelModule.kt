package tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.subscomponents.main.subscomponents.home.modules.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.subscomponents.main.subscomponents.home.qualifiers.HomeFragment
import tv.wfc.livestreamsales.di.mapkeys.ViewModelKey
import tv.wfc.livestreamsales.di.scopes.FragmentScope
import tv.wfc.livestreamsales.viewmodels.home.HomeViewModel
import tv.wfc.livestreamsales.viewmodels.home.IHomeViewModel

@Module
abstract class HomeViewModelModule {
    companion object{
        @FragmentScope
        @Provides
        @JvmStatic
        internal fun provideIHomeViewModel(
            @HomeFragment
            fragment: Fragment,
            viewModelProviderFactory: ViewModelProvider.Factory
        ): IHomeViewModel{
            return ViewModelProvider(
                fragment,
                viewModelProviderFactory
            )[HomeViewModel::class.java]
        }
    }

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    internal abstract fun bindHomeViewModelIntoMap(
        homeViewModel: HomeViewModel
    ): ViewModel
}