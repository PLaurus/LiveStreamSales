package tv.wfc.livestreamsales.features.authorization.paymentcardinformation.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import tv.wfc.livestreamsales.features.authorization.paymentcardinformation.di.modules.viewmodel.RegistrationPaymentCardInformationViewModelModule
import tv.wfc.livestreamsales.features.authorization.paymentcardinformation.di.qualifiers.RegistrationPaymentCardInformationFragment
import tv.wfc.livestreamsales.features.authorization.paymentcardinformation.di.scope.RegistrationPaymentCardInformationFeatureScope

@RegistrationPaymentCardInformationFeatureScope
@Subcomponent(modules = [
    RegistrationPaymentCardInformationViewModelModule::class
])
interface RegistrationPaymentCardInformationComponent {
    @Subcomponent.Factory
    interface Factory{
        fun create(
            @BindsInstance
            @RegistrationPaymentCardInformationFragment
            fragment: Fragment
        ): RegistrationPaymentCardInformationComponent
    }

    fun inject(fragment: tv.wfc.livestreamsales.features.authorization.paymentcardinformation.ui.RegistrationPaymentCardInformationFragment)
}