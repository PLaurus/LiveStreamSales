package tv.wfc.livestreamsales.features.my_broadcasts.di.modules

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import tv.wfc.livestreamsales.application.di.modules.viewmodelprovider.mapkeys.ViewModelKey
import tv.wfc.livestreamsales.features.my_broadcasts.di.qualifiers.MyBroadcastsFragment
import tv.wfc.livestreamsales.features.my_broadcasts.di.scope.MyBroadcastsFeatureScope
import tv.wfc.livestreamsales.features.my_broadcasts.view_model.IMyBroadcastsViewModel
import tv.wfc.livestreamsales.features.my_broadcasts.view_model.MyBroadcastsViewModel

@Module
abstract class ViewModelModule {
    companion object {
        @Provides
        @MyBroadcastsFeatureScope
        internal fun provideIMyBroadcastsViewModel(
            @MyBroadcastsFragment
            fragment: Fragment,
            viewModelProviderFactory: ViewModelProvider.Factory
        ): IMyBroadcastsViewModel {
            return ViewModelProvider(
                fragment,
                viewModelProviderFactory
            )[MyBroadcastsViewModel::class.java]
        }
    }

    @Binds
    @IntoMap
    @ViewModelKey(MyBroadcastsViewModel::class)
    internal abstract fun bindMyBroadcastsViewModelIntoMap(
        viewModel: MyBroadcastsViewModel
    ): ViewModel
}