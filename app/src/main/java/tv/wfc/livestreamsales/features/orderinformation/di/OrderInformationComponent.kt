package tv.wfc.livestreamsales.features.orderinformation.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import tv.wfc.livestreamsales.features.orderinformation.di.modules.diffutils.DiffUtilsModule
import tv.wfc.livestreamsales.features.orderinformation.di.modules.viewmodel.OrderInformationViewModelModule
import tv.wfc.livestreamsales.features.orderinformation.di.qualifiers.OrderInformationFragment
import tv.wfc.livestreamsales.features.orderinformation.di.scope.OrderInformationFeatureScope

@OrderInformationFeatureScope
@Subcomponent(modules = [
    OrderInformationViewModelModule::class,
    DiffUtilsModule::class
])
interface OrderInformationComponent {
    @Subcomponent.Factory
    interface Factory{
        fun create(
            @BindsInstance
            @OrderInformationFragment
            fragment: Fragment
        ): OrderInformationComponent
    }

    fun inject(fragment: tv.wfc.livestreamsales.features.orderinformation.ui.OrderInformationFragment)
}