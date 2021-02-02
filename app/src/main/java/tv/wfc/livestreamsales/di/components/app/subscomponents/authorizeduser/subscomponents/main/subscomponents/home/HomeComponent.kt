package tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.subscomponents.main.subscomponents.home

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.subscomponents.main.subscomponents.home.modules.subcomponents.HomeSubComponentsModule
import tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.subscomponents.main.subscomponents.home.modules.viewmodel.HomeViewModelModule
import tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.subscomponents.main.subscomponents.home.qualifiers.HomeFragment
import tv.wfc.livestreamsales.di.scopes.FragmentScope

@FragmentScope
@Subcomponent(modules = [
    HomeSubComponentsModule::class,
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

    fun inject(fragment: tv.wfc.livestreamsales.ui.activity.main.fragments.home.HomeFragment)
}