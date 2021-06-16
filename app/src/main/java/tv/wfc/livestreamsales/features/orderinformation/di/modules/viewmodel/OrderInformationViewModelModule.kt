package tv.wfc.livestreamsales.features.orderinformation.di.modules.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import tv.wfc.livestreamsales.application.di.modules.viewmodelprovider.mapkeys.ViewModelKey
import tv.wfc.livestreamsales.features.orderinformation.di.qualifiers.OrderInformationFragment
import tv.wfc.livestreamsales.features.orderinformation.di.scope.OrderInformationFeatureScope
import tv.wfc.livestreamsales.features.orderinformation.viewmodel.IOrderInformationViewModel
import tv.wfc.livestreamsales.features.orderinformation.viewmodel.OrderInformationViewModel

@Module
abstract class OrderInformationViewModelModule {
    companion object{
        @OrderInformationFeatureScope
        @Provides
        internal fun provideIOrderInformationViewModel(
            @OrderInformationFragment
            fragment: Fragment,
            viewModelProviderFactory: ViewModelProvider.Factory
        ): IOrderInformationViewModel{
            return ViewModelProvider(
                fragment,
                viewModelProviderFactory
            )[OrderInformationViewModel::class.java]
        }
    }

    @Binds
    @IntoMap
    @ViewModelKey(OrderInformationViewModel::class)
    internal abstract fun bindOrderInformationViewModelIntoMap(
        orderInformationViewModel: OrderInformationViewModel
    ): ViewModel
}