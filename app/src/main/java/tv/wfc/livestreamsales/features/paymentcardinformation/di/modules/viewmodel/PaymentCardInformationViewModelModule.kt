package tv.wfc.livestreamsales.features.paymentcardinformation.di.modules.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import tv.wfc.livestreamsales.application.di.modules.viewmodelprovider.mapkeys.ViewModelKey
import tv.wfc.livestreamsales.features.paymentcardinformation.di.qualifiers.PaymentCardInformationFragment
import tv.wfc.livestreamsales.features.paymentcardinformation.di.scope.PaymentCardInformationFeatureScope
import tv.wfc.livestreamsales.features.paymentcardinformation.viewmodel.IPaymentCardInformationViewModel
import tv.wfc.livestreamsales.features.paymentcardinformation.viewmodel.PaymentCardInformationViewModel

@Module
abstract class PaymentCardInformationViewModelModule {
    companion object{
        @PaymentCardInformationFeatureScope
        @Provides
        fun provideIRegistrationPaymentCardInformationViewModel(
            @PaymentCardInformationFragment
            fragment: Fragment,
            viewModelProviderFactory: ViewModelProvider.Factory
        ): IPaymentCardInformationViewModel {
            return ViewModelProvider(
                fragment,
                viewModelProviderFactory
            )[PaymentCardInformationViewModel::class.java]
        }
    }

    @Binds
    @IntoMap
    @ViewModelKey(PaymentCardInformationViewModel::class)
    abstract fun bindRegistrationPaymentCardInformationViewModelIntoMap(
        registrationPaymentCardInformationViewModel: PaymentCardInformationViewModel
    ): ViewModel
}