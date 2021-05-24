package tv.wfc.livestreamsales.features.authorization.paymentcardinformation.di.modules.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import tv.wfc.livestreamsales.application.di.modules.viewmodelprovider.mapkeys.ViewModelKey
import tv.wfc.livestreamsales.features.authorization.paymentcardinformation.di.qualifiers.RegistrationPaymentCardInformationFragment
import tv.wfc.livestreamsales.features.authorization.paymentcardinformation.di.scope.RegistrationPaymentCardInformationFeatureScope
import tv.wfc.livestreamsales.features.authorization.paymentcardinformation.viewmodel.IRegistrationPaymentCardInformationViewModel
import tv.wfc.livestreamsales.features.authorization.paymentcardinformation.viewmodel.RegistrationPaymentCardInformationViewModel

@Module
abstract class RegistrationPaymentCardInformationViewModelModule {
    companion object{
        @RegistrationPaymentCardInformationFeatureScope
        @Provides
        fun provideIRegistrationPaymentCardInformationViewModel(
            @RegistrationPaymentCardInformationFragment
            fragment: Fragment,
            viewModelProviderFactory: ViewModelProvider.Factory
        ): IRegistrationPaymentCardInformationViewModel {
            return ViewModelProvider(
                fragment,
                viewModelProviderFactory
            )[RegistrationPaymentCardInformationViewModel::class.java]
        }
    }

    @Binds
    @IntoMap
    @ViewModelKey(RegistrationPaymentCardInformationViewModel::class)
    abstract fun bindRegistrationPaymentCardInformationViewModelIntoMap(
        registrationPaymentCardInformationViewModel: RegistrationPaymentCardInformationViewModel
    ): ViewModel
}