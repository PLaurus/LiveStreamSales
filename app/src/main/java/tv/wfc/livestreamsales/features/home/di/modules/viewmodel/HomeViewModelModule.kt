package tv.wfc.livestreamsales.features.home.di.modules.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import tv.wfc.livestreamsales.features.home.di.qualifiers.HomeFragment
import tv.wfc.livestreamsales.application.di.modules.viewmodelprovider.mapkeys.ViewModelKey
import tv.wfc.livestreamsales.features.home.di.scope.HomeFeatureScope
import tv.wfc.livestreamsales.features.home.viewmodel.HomeViewModel
import tv.wfc.livestreamsales.features.home.viewmodel.IHomeViewModel

@Module
abstract class HomeViewModelModule {
    companion object{
        @HomeFeatureScope
        @Provides
        @JvmStatic
        internal fun provideIHomeViewModel(
            @HomeFragment
            fragment: Fragment,
            viewModelProviderFactory: ViewModelProvider.Factory
        ): IHomeViewModel {
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