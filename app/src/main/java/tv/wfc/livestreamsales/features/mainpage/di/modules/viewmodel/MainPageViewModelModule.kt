package tv.wfc.livestreamsales.features.mainpage.di.modules.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import tv.wfc.livestreamsales.features.mainpage.di.qualifiers.MainPageFragment
import tv.wfc.livestreamsales.application.di.modules.viewmodelprovider.mapkeys.ViewModelKey
import tv.wfc.livestreamsales.features.mainpage.di.scope.MainPageFeatureScope
import tv.wfc.livestreamsales.features.mainpage.viewmodel.MainPageViewModel
import tv.wfc.livestreamsales.features.mainpage.viewmodel.IMainPageViewModel

@Module
abstract class MainPageViewModelModule {
    companion object{
        @MainPageFeatureScope
        @Provides
        @JvmStatic
        internal fun provideIMainPageViewModel(
            @MainPageFragment
            fragment: Fragment,
            viewModelProviderFactory: ViewModelProvider.Factory
        ): IMainPageViewModel {
            return ViewModelProvider(
                fragment,
                viewModelProviderFactory
            )[MainPageViewModel::class.java]
        }
    }

    @Binds
    @IntoMap
    @ViewModelKey(MainPageViewModel::class)
    internal abstract fun bindMainPageViewModelIntoMap(
        mainPageViewModel: MainPageViewModel
    ): ViewModel
}