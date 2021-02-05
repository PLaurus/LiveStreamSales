package tv.wfc.livestreamsales.features.home.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import tv.wfc.livestreamsales.features.home.di.modules.viewmodel.HomeViewModelModule
import tv.wfc.livestreamsales.features.home.di.qualifiers.HomeFragment
import tv.wfc.livestreamsales.features.home.di.scope.HomeFeatureScope

@HomeFeatureScope
@Subcomponent(modules = [
    HomeViewModelModule::class
])
interface HomeComponent {
    @Subcomponent.Factory
    interface Factory{
        fun create(
            @BindsInstance
            @HomeFragment
            fragment: Fragment
        ): HomeComponent
    }

    fun inject(fragment: tv.wfc.livestreamsales.features.home.ui.HomeFragment)
}