package tv.wfc.livestreamsales.features.myorders.di.modules.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import tv.wfc.livestreamsales.application.di.modules.viewmodelprovider.mapkeys.ViewModelKey
import tv.wfc.livestreamsales.features.myorders.di.qualifiers.MyOrdersFragment
import tv.wfc.livestreamsales.features.myorders.di.scope.MyOrdersFeatureScope
import tv.wfc.livestreamsales.features.myorders.viewmodel.IMyOrdersViewModel
import tv.wfc.livestreamsales.features.myorders.viewmodel.MyOrdersViewModel

@Module
abstract class MyOrdersViewModelModule {
    companion object{
        @MyOrdersFeatureScope
        @Provides
        internal fun provideIMyOrdersViewModel(
            @MyOrdersFragment
            fragment: Fragment,
            viewModelProviderFactory: ViewModelProvider.Factory
        ): IMyOrdersViewModel {
            return ViewModelProvider(
                fragment,
                viewModelProviderFactory
            )[MyOrdersViewModel::class.java]
        }
    }

    @Binds
    @IntoMap
    @ViewModelKey(MyOrdersViewModel::class)
    internal abstract fun bindMyOrdersViewModelIntoMap(
        myOrdersViewModel: MyOrdersViewModel
    ): ViewModel
}