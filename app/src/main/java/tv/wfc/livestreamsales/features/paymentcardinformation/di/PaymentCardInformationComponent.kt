package tv.wfc.livestreamsales.features.paymentcardinformation.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import tv.wfc.livestreamsales.features.paymentcardinformation.di.modules.viewmodel.PaymentCardInformationViewModelModule
import tv.wfc.livestreamsales.features.paymentcardinformation.di.qualifiers.PaymentCardInformationFragment
import tv.wfc.livestreamsales.features.paymentcardinformation.di.scope.PaymentCardInformationFeatureScope

@PaymentCardInformationFeatureScope
@Subcomponent(modules = [
    PaymentCardInformationViewModelModule::class
])
interface PaymentCardInformationComponent {
    @Subcomponent.Factory
    interface Factory{
        fun create(
            @BindsInstance
            @PaymentCardInformationFragment
            fragment: Fragment
        ): PaymentCardInformationComponent
    }

    fun inject(fragment: tv.wfc.livestreamsales.features.paymentcardinformation.ui.PaymentCardInformationFragment)
}