package tv.wfc.livestreamsales.features.profile.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import tv.wfc.livestreamsales.features.profile.di.modules.ProfileViewModelModule
import tv.wfc.livestreamsales.features.profile.di.scope.ProfileFeature
import tv.wfc.livestreamsales.features.profile.ui.ProfileFragment

@ProfileFeature
@Subcomponent(modules = [
    ProfileViewModelModule::class
])
interface ProfileComponent {
    @Subcomponent.Factory
    interface Factory{
        fun create(
            @BindsInstance
            fragment: Fragment
        ): ProfileComponent
    }

    fun inject(fragment: ProfileFragment)
}