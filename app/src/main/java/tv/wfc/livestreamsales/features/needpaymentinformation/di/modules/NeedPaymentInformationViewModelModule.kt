package tv.wfc.livestreamsales.features.needpaymentinformation.di.modules

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import tv.wfc.livestreamsales.application.di.modules.viewmodelprovider.mapkeys.ViewModelKey
import tv.wfc.livestreamsales.features.needpaymentinformation.di.qualifiers.NeedPaymentInformationDialogFragment
import tv.wfc.livestreamsales.features.needpaymentinformation.di.scope.NeedPaymentInformationScope
import tv.wfc.livestreamsales.features.needpaymentinformation.viewmodel.INeedPaymentInformationViewModel
import tv.wfc.livestreamsales.features.needpaymentinformation.viewmodel.NeedPaymentInformationViewModel

@Module
abstract class NeedPaymentInformationViewModelModule {
    companion object{
        @NeedPaymentInformationScope
        @Provides
        fun provideINeedPaymentInformationViewModel(
            @NeedPaymentInformationDialogFragment
            fragment: Fragment,
            viewModelProviderFactory: ViewModelProvider.Factory
        ): INeedPaymentInformationViewModel {
            return ViewModelProvider(
                fragment,
                viewModelProviderFactory
            )[NeedPaymentInformationViewModel::class.java]
        }
    }

    @Binds
    @IntoMap
    @ViewModelKey(NeedPaymentInformationViewModel::class)
    abstract fun bindNeedPaymentInformationViewModelIntoMap(
        needPaymentInformationViewModel: NeedPaymentInformationViewModel
    ): ViewModel
}