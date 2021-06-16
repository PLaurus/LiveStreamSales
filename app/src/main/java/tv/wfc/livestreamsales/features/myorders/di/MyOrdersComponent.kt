package tv.wfc.livestreamsales.features.myorders.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import tv.wfc.livestreamsales.features.myorders.di.modules.diffutils.DiffUtilsModule
import tv.wfc.livestreamsales.features.myorders.di.modules.viewmodel.MyOrdersViewModelModule
import tv.wfc.livestreamsales.features.myorders.di.qualifiers.MyOrdersFragment
import tv.wfc.livestreamsales.features.myorders.di.scope.MyOrdersFeatureScope

@MyOrdersFeatureScope
@Subcomponent(modules = [
    MyOrdersViewModelModule::class,
    DiffUtilsModule::class
])
interface MyOrdersComponent {
    @Subcomponent.Factory
    interface Factory{
        fun create(
            @BindsInstance
            @MyOrdersFragment
            fragment: Fragment
        ): MyOrdersComponent
    }

    fun inject(fragment: tv.wfc.livestreamsales.features.myorders.ui.MyOrdersFragment)
}