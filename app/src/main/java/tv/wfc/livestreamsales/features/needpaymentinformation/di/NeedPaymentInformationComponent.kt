package tv.wfc.livestreamsales.features.needpaymentinformation.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import tv.wfc.livestreamsales.features.needpaymentinformation.di.modules.NeedPaymentInformationViewModelModule
import tv.wfc.livestreamsales.features.needpaymentinformation.di.qualifiers.NeedPaymentInformationDialogFragment
import tv.wfc.livestreamsales.features.needpaymentinformation.di.scope.NeedPaymentInformationScope

@NeedPaymentInformationScope
@Subcomponent(modules = [
    NeedPaymentInformationViewModelModule::class
])
interface NeedPaymentInformationComponent {
    @Subcomponent.Factory
    interface Factory{
        fun create(
            @BindsInstance
            @NeedPaymentInformationDialogFragment
            fragment: Fragment
        ): NeedPaymentInformationComponent
    }

    fun inject(fragment: tv.wfc.livestreamsales.features.needpaymentinformation.ui.NeedPaymentInformationDialogFragment)
}